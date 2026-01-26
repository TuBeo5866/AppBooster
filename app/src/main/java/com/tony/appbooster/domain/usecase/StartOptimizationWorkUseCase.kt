package com.tony.appbooster.domain.usecase

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.domain.scheduler.OptimizationWorkScheduler
import javax.inject.Inject

/**
 * Enqueues the foreground optimization WorkManager job.
 *
 * Business purpose:
 * - Centralizes work scheduling behind a testable abstraction.
 * - Keeps ViewModels free of WorkManager details.
 *
 * @property scheduler Scheduler responsible for enqueuing and canceling work.
 */
class StartOptimizationWorkUseCase @Inject constructor(
    private val scheduler: OptimizationWorkScheduler
) {

    /**
     * Enqueues optimization as unique work.
     *
     * @param mode Optimization mode to execute.
     * @return [Resource.Success] when the request is enqueued.
     */
    operator fun invoke(mode: AppOptimizationType): Resource<Unit> {
        scheduler.enqueue(mode)
        return Resource.Success(Unit)
    }
}
