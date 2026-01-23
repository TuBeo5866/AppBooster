package com.tony.appbooster.presentation.viewmodel.shizuku

import com.tony.appbooster.domain.model.shizuku.ShizukuState

/**
 * UI state for the Shizuku setup screen.
 */
data class ShizukuSetupUiModel(
    val shizukuState: ShizukuState = ShizukuState.NotRunning,
    val isCheckingState: Boolean = false,
    val setupStep: ShizukuSetupStep = ShizukuSetupStep.CHECK_STATUS
)

/**
 * Steps in the Shizuku setup flow.
 */
enum class ShizukuSetupStep {
    /** Initial status check */
    CHECK_STATUS,
    /** Shizuku needs to be installed */
    INSTALL_SHIZUKU,
    /** Shizuku is installed but service not running */
    START_SERVICE,
    /** Service running, permission needed */
    GRANT_PERMISSION,
    /** All set up and ready */
    READY
}
