package com.tony.appbooster.presentation.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager

/**
 * Receives the Stop action from the optimization notification and cancels the running Worker.
 *
 * Business purpose: allow the user to stop optimization from the system notification.
 * Cancelling the Worker triggers [OptimizationWorker.onStopped], which requests repository-side
 * cancellation so the UI reflects the stop immediately.
 */
class OptimizationWorkerStopReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val workId = intent.getStringExtra(EXTRA_WORK_ID) ?: return
        WorkManager.getInstance(context).cancelWorkById(java.util.UUID.fromString(workId))
    }

    companion object {
        const val EXTRA_WORK_ID = "extra_work_id"
    }
}
