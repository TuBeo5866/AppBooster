package com.tony.appbooster.domain.usecase.shizuku

import com.tony.appbooster.domain.client.ShizukuShellClient
import com.tony.appbooster.domain.model.shizuku.ShizukuState
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Observes the current Shizuku service state for UI updates.
 */
class ObserveShizukuStateUseCase @Inject constructor(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * @return StateFlow emitting Shizuku state changes.
     */
    operator fun invoke(): StateFlow<ShizukuState> = shizukuClient.state
}

/**
 * Refreshes the Shizuku state by checking service availability.
 */
class RefreshShizukuStateUseCase @Inject constructor(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Checks current Shizuku state and updates observers.
     */
    suspend operator fun invoke() = shizukuClient.refreshState()
}

/**
 * Requests Shizuku permission from the user.
 */
class RequestShizukuPermissionUseCase @Inject constructor(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Triggers the Shizuku permission request dialog.
     */
    suspend operator fun invoke() = shizukuClient.requestPermission()
}

/**
 * Opens the Shizuku download/install page.
 */
class OpenShizukuInstallPageUseCase @Inject constructor(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Launches browser or Play Store to install Shizuku.
     */
    operator fun invoke() = shizukuClient.openShizukuInstallPage()
}

/**
 * Opens the Shizuku app for the user to start the service.
 */
class OpenShizukuAppUseCase @Inject constructor(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Launches the Shizuku app.
     */
    operator fun invoke() = shizukuClient.openShizukuApp()
}
