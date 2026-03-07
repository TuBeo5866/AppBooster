package com.tony.appbooster.presentation.viewmodel.shizuku

import com.tony.appbooster.domain.model.shizuku.ShizukuState

/**
 * Immutable UI state for the Shizuku setup screen.
 *
 * @property shizukuState Current runtime state of the Shizuku service.
 * @property isCheckingState Whether a background status check is in progress.
 * @property setupStep The active wizard step driving the step card and progress indicator.
 */
data class ShizukuSetupUiModel(
    val shizukuState: ShizukuState = ShizukuState.NotRunning,
    val isCheckingState: Boolean = false,
    val setupStep: ShizukuSetupStep = ShizukuSetupStep.CHECK_STATUS
)

