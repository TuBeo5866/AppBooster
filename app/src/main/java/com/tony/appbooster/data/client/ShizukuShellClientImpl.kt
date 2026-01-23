package com.tony.appbooster.data.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.net.toUri
import com.tony.appbooster.BuildConfig
import com.tony.appbooster.IShellService
import com.tony.appbooster.domain.client.ShizukuShellClient
import com.tony.appbooster.domain.model.shizuku.ShellResult
import com.tony.appbooster.domain.model.shizuku.ShizukuState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [ShizukuShellClient] that uses Shizuku API for privileged shell access.
 *
 * Uses Shizuku's UserService mechanism to run a shell service with elevated privileges.
 * Commands executed through this service run with shell (ADB) UID, enabling system-level
 * operations like app optimization.
 */
@Singleton
class ShizukuShellClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : ShizukuShellClient {

    private val _state = MutableStateFlow<ShizukuState>(ShizukuState.NotRunning)
    override val state: StateFlow<ShizukuState> = _state.asStateFlow()

    private var shellService: IShellService? = null
    private val serviceMutex = Mutex()

    private val userServiceArgs = Shizuku.UserServiceArgs(
        ComponentName(BuildConfig.APPLICATION_ID, ShellService::class.java.name)
    )
        .daemon(false)
        .processNameSuffix("shell")
        .debuggable(BuildConfig.DEBUG)
        .version(BuildConfig.VERSION_CODE)

    private val userServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d(TAG, "ShellService connected")
            shellService = IShellService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG, "ShellService disconnected")
            shellService = null
        }
    }

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        Log.d(TAG, "Shizuku binder received")
        updateState()
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        Log.d(TAG, "Shizuku binder dead")
        shellService = null
        _state.value = ShizukuState.NotRunning
    }

    private val permissionResultListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        val granted = grantResult == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "Permission result received: requestCode=$requestCode, granted=$granted")
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (granted) {
                Log.i(TAG, "Shizuku permission granted!")
                _state.value = ShizukuState.Ready
                bindShellService()
            } else {
                Log.w(TAG, "Shizuku permission denied")
                _state.value = ShizukuState.PermissionRequired
            }
        }
    }

    init {
        Shizuku.addBinderReceivedListener(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
        Shizuku.addRequestPermissionResultListener(permissionResultListener)
        updateState()
    }

    override suspend fun refreshState() {
        withContext(ioDispatcher) {
            updateState()
        }
    }

    private fun updateState() {
        val isInstalled = isShizukuInstalled()
        val isBinderAlive = try { Shizuku.pingBinder() } catch (e: Exception) {
            Log.e(TAG, "Error pinging binder", e)
            false
        }
        val hasPerms = if (isBinderAlive) hasPermission() else false

        Log.d(TAG, "updateState: installed=$isInstalled, binderAlive=$isBinderAlive, hasPermission=$hasPerms")

        _state.value = when {
            !isInstalled -> ShizukuState.NotInstalled
            !isBinderAlive -> ShizukuState.NotRunning
            !hasPerms -> ShizukuState.PermissionRequired
            else -> ShizukuState.Ready
        }
        Log.d(TAG, "Shizuku state updated: ${_state.value}")

        // Bind/unbind service based on state
        if (_state.value == ShizukuState.Ready && shellService == null) {
            bindShellService()
        }
    }

    private fun bindShellService() {
        try {
            Shizuku.bindUserService(userServiceArgs, userServiceConnection)
            Log.d(TAG, "Binding ShellService...")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bind ShellService", e)
        }
    }

    private fun unbindShellService() {
        try {
            Shizuku.unbindUserService(userServiceArgs, userServiceConnection, true)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unbind ShellService", e)
        }
    }

    private fun isShizukuInstalled(): Boolean {
        return try {
            context.packageManager.getPackageInfo(SHIZUKU_PACKAGE, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun hasPermission(): Boolean {
        return try {
            if (Shizuku.isPreV11()) {
                Log.d(TAG, "Shizuku is pre-v11, using legacy permission check")
                // For pre-v11, check if we can ping binder - if yes, we have permission
                false
            } else {
                val result = Shizuku.checkSelfPermission()
                val granted = result == PackageManager.PERMISSION_GRANTED
                Log.d(TAG, "Shizuku permission check result: $result (granted=$granted)")
                granted
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking Shizuku permission", e)
            false
        }
    }

    override suspend fun requestPermission() {
        Log.d(TAG, "requestPermission() called")

        // Check if Shizuku binder is alive
        val binderAlive = try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            Log.e(TAG, "Error pinging Shizuku binder", e)
            false
        }

        if (!binderAlive) {
            Log.w(TAG, "Shizuku binder not alive, cannot request permission")
            _state.value = ShizukuState.NotRunning
            return
        }

        // Check if already have permission
        if (hasPermission()) {
            Log.d(TAG, "Already have Shizuku permission")
            _state.value = ShizukuState.Ready
            return
        }

        // For Shizuku 11+, we use the new permission request API
        if (Shizuku.isPreV11()) {
            Log.w(TAG, "Shizuku version is pre-v11, not supported")
            _state.value = ShizukuState.Error("Shizuku version too old. Please update Shizuku.")
            return
        }

        // Get Shizuku version for debugging
        try {
            val version = Shizuku.getVersion()
            Log.d(TAG, "Shizuku version: $version")
        } catch (e: Exception) {
            Log.w(TAG, "Could not get Shizuku version", e)
        }

        Log.d(TAG, "Requesting Shizuku permission with request code: $PERMISSION_REQUEST_CODE")

        try {
            // Request permission - this should trigger Shizuku to show a dialog
            Shizuku.requestPermission(PERMISSION_REQUEST_CODE)
            Log.d(TAG, "Permission request sent to Shizuku successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error requesting Shizuku permission", e)
            _state.value = ShizukuState.Error(e.message ?: "Failed to request permission")
        }
    }

    override fun isReady(): Boolean = _state.value == ShizukuState.Ready

    override suspend fun execute(command: String): ShellResult = withContext(ioDispatcher) {
        serviceMutex.withLock {
            if (!isReady()) {
                throw IllegalStateException("Shizuku is not ready. Current state: ${_state.value}")
            }

            Log.d(TAG, "Executing command: $command")

            val service = shellService
            if (service != null) {
                try {
                    val result = service.executeCommand(command)
                    ShellResult(
                        exitCode = result[0].toIntOrNull() ?: -1,
                        output = result[1],
                        error = result[2]
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Command execution via service failed", e)
                    // Fallback or retry binding
                    bindShellService()
                    ShellResult(
                        exitCode = -1,
                        output = "",
                        error = "Service connection lost: ${e.message}"
                    )
                }
            } else {
                // Service not yet connected, try to bind
                bindShellService()
                ShellResult(
                    exitCode = -1,
                    output = "",
                    error = "ShellService not connected. Please try again."
                )
            }
        }
    }

    override fun executeStreaming(command: String): Flow<Result<String>> = flow {
        if (!isReady()) {
            emit(Result.failure(IllegalStateException("Shizuku is not ready")))
            return@flow
        }

        // For streaming, we execute the full command and emit lines
        val result = execute(command)
        if (result.isSuccess) {
            result.output.lineSequence()
                .filter { it.isNotEmpty() }
                .forEach { line ->
                    emit(Result.success(line))
                }
        } else {
            emit(Result.failure(RuntimeException(result.error)))
        }
    }

    override fun openShizukuInstallPage() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "https://shizuku.rikka.app/download/".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openShizukuApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(SHIZUKU_PACKAGE)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            openShizukuInstallPage()
        }
    }

    companion object {
        private const val TAG = "ShizukuShellClient"
        private const val SHIZUKU_PACKAGE = "moe.shizuku.privileged.api"
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}
