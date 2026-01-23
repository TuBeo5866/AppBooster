package com.tony.appbooster.domain.model.shizuku

/**
 * Represents the current state of Shizuku service availability and authorization.
 */
sealed interface ShizukuState {

    /**
     * Shizuku app is not installed on the device.
     */
    data object NotInstalled : ShizukuState

    /**
     * Shizuku app is installed but the service is not running.
     * User needs to start it via ADB or root.
     */
    data object NotRunning : ShizukuState

    /**
     * Shizuku service is running but this app hasn't been granted permission.
     */
    data object PermissionRequired : ShizukuState

    /**
     * Shizuku is fully authorized and ready to execute privileged commands.
     */
    data object Ready : ShizukuState

    /**
     * An error occurred while checking or using Shizuku.
     */
    data class Error(val message: String) : ShizukuState
}

/**
 * Result of a Shizuku shell command execution.
 */
data class ShellResult(
    val exitCode: Int,
    val output: String,
    val error: String
) {
    val isSuccess: Boolean get() = exitCode == 0
}
