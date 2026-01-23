package com.tony.appbooster.presentation.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tony.appbooster.R
import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.domain.repository.AdbRepository
import com.tony.appbooster.presentation.activity.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Foreground [CoroutineWorker] that runs the app optimization workflow.
 *
 * Business purpose:
 * - Keeps optimization running even when the app is backgrounded.
 * - Shows a persistent notification with the currently optimized package.
 * - Exposes a Stop action that cancels this Worker. Cancellation also requests repository-side
 *   cancellation so the UI reflects the stop immediately when opened.
 *
 * The Worker reuses the singleton [AdbRepository] instance; since the UI subscribes to its
 * progress/log flows, state stays in sync across app/worker.
 *
 * @property repository Repository coordinating shell connection and optimization progress.
 * @constructor Creates the worker with injected dependencies.
 */
@HiltWorker
class OptimizationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: AdbRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        val optimizationModeRaw = inputData.getString(KEY_OPTIMIZATION_MODE)
            ?: return@coroutineScope Result.failure()

        val mode = parseOptimizationMode(optimizationModeRaw) ?: return@coroutineScope Result.failure()

        createNotificationChannelIfNeeded()

        // Start foreground immediately.
        setForeground(createForegroundInfo(currentPackage = null))

        // Update notification whenever the current package changes.
        val notificationJob: Job = launch {
            repository.optimizationProgress
                .map { it.currentAppPackage }
                .distinctUntilChangedBy { it }
                .collect { currentPackage ->
                    setForeground(createForegroundInfo(currentPackage = currentPackage.ifBlank { null }))
                }
        }

        try {
            when (repository.ensureConnected()) {
                is Resource.Success -> Unit
                is Resource.Error -> return@coroutineScope Result.failure()
            }

            if (isStopped) {
                repository.cancelOptimization()
                return@coroutineScope Result.success()
            }

            when (repository.executeOptimizationCommand(mode)) {
                is Resource.Success -> Result.success()
                is Resource.Error -> Result.failure()
            }
        } catch (_: CancellationException) {
            // WorkManager cancellation (e.g., notification stop) lands here.
            repository.cancelOptimization()
            Result.success()
        } finally {
            notificationJob.cancel()
        }
    }

    private fun createForegroundInfo(currentPackage: String?): ForegroundInfo {
        val notification = buildNotification(currentPackage)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
    }

    private fun buildNotification(currentPackage: String?): Notification {
        val title = applicationContext.getString(R.string.app_name)
        val contentText = currentPackage?.takeIf { it.isNotBlank() }
            ?: applicationContext.getString(R.string.optimization_notification_preparing)

        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(applicationContext, OptimizationWorkerStopReceiver::class.java)
            .putExtra(OptimizationWorkerStopReceiver.EXTRA_WORK_ID, id.toString())

        val stopPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(contentText)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(contentIntent)
            .addAction(
                NotificationCompat.Action(
                    0,
                    applicationContext.getString(R.string.optimization_notification_stop),
                    stopPendingIntent
                )
            )
            .build()
    }

    private fun createNotificationChannelIfNeeded() {
        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            applicationContext.getString(R.string.optimization_notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = applicationContext.getString(R.string.optimization_notification_channel_description)
        }

        manager.createNotificationChannel(channel)
    }

    private fun parseOptimizationMode(value: String): AppOptimizationType? {
        return when (value) {
            AppOptimizationType.SPEED_PROFILE.value -> AppOptimizationType.SPEED_PROFILE
            AppOptimizationType.FULL_OPTIMIZATION.value -> AppOptimizationType.FULL_OPTIMIZATION
            else -> null
        }
    }

    companion object {
        const val KEY_OPTIMIZATION_MODE = "optimization_mode"

        private const val NOTIFICATION_CHANNEL_ID = "optimization"
        private const val NOTIFICATION_ID = 1001

        /**
         * Enqueues a unique optimization worker.
         *
         * @param context Context used to enqueue work.
         * @param mode Optimization mode to run.
         */
        fun enqueue(context: Context, mode: AppOptimizationType) {
            val request = androidx.work.OneTimeWorkRequestBuilder<OptimizationWorker>()
                .setInputData(
                    androidx.work.workDataOf(KEY_OPTIMIZATION_MODE to mode.value)
                )
                .addTag(TAG)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(UNIQUE_WORK_NAME, androidx.work.ExistingWorkPolicy.REPLACE, request)
        }

        /**
         * Cancels the currently running optimization worker, if any.
         *
         * @param context Context used to cancel work.
         */
        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME)
        }

        private const val UNIQUE_WORK_NAME = "optimization_work"
        const val TAG = "optimization"
    }
}
