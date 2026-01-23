package com.tony.appbooster.presentation.viewmodel.main

import com.tony.appbooster.domain.model.common.OptimizationProgress
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.domain.repository.AdbConnectionState

/**
 * UI\-level model aggregating ADB connection and optimization data
 * required by the main setup screen.
 *
 * @param connectionState Current ADB connection status.
 * @param logs Shell output lines visible in the console area.
 * @param optimizationProgress Progress of the active optimization job.
 */
data class MainUiModel(
    val connectionState: AdbConnectionState = AdbConnectionState.Disconnected,
    val logs: List<String> = emptyList(),
    val optimizationProgress: OptimizationProgress = OptimizationProgress(),
    val optimizationMode: AppOptimizationType = AppOptimizationType.SPEED_PROFILE,
    val adbPort: Int? = null,
    val adbHost: String? = null,
    val adbPairingCode: Int? = null
)
