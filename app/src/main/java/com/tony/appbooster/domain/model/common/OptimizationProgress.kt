package com.tony.appbooster.domain.model.common

/**
 * Represents the current state of app optimization progress.
 *
 * @property isRunning Whether optimization is currently in progress.
 * @property isCompleted Whether optimization has finished successfully.
 * @property currentAppPackage The package name of the app currently being optimized.
 * @property progress Progress value from 0.0 to 1.0.
 * @property processedCount Number of apps already optimized.
 * @property totalCount Total number of apps to optimize.
 */
data class OptimizationProgress(
    val isRunning: Boolean = false,
    val isCompleted: Boolean = false,
    val currentAppPackage: String = "",
    val progress: Float = 0f,
    val processedCount: Int = 0,
    val totalCount: Int = 0
)
