package com.tony.appbooster.domain.scheduler

import com.tony.appbooster.domain.model.settings.AppOptimizationType

/**
 * Schedules and cancels foreground analysis work.
 *
 * Business purpose:
 * - Moves WorkManager orchestration out of ViewModels.
 * - Allows the same behavior to be reused when analysis is required as a prerequisite.
 */
interface AnalysisWorkScheduler {

    /**
     * Enqueues a unique analysis run.
     *
     * @param mode Optimization mode used for analysis criteria.
     */
    fun enqueue(mode: AppOptimizationType)

    /**
     * Cancels the currently running analysis work, if any.
     */
    fun cancel()
}
