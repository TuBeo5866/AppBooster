package com.tony.appbooster.domain.usecase

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.scheduler.OptimizationWorkScheduler
import javax.inject.Inject

/**
 * Cancels any running optimization work.
 *
 * Business purpose:
 * - Keeps WorkManager cancellation details out of ViewModels.
 * - Allows stop to be triggered consistently from UI and notifications.
 *
 * @property scheduler Scheduler responsible for canceling optimization work.
 */
class CancelOptimizationWorkUseCase @Inject constructor(
    private val scheduler: OptimizationWorkScheduler
) {

    /**
     * @return [Resource.Success] after issuing cancellation.
     */
    operator fun invoke(): Resource<Unit> {
        scheduler.cancel()
        return Resource.Success(Unit)
    }
}
