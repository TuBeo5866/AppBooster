package com.tony.appbooster.domain.usecase.internal

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.usecase.adb.ConnectAdbUseCase

/**
 * Provides lightweight, reusable orchestration helpers for WorkManager-backed domain flows.
 *
 * Business purpose:
 * - Reduces duplicated precondition/orchestration logic across "start" use cases.
 * - Keeps public use case APIs stable (each feature still exposes its own *UseCase).
 * - Centralizes the connectivity prerequisite before scheduling foreground work.
 */
internal object WorkOrchestrationHelpers {

    /**
     * Ensures ADB/Shizuku connectivity, then runs the provided scheduling block.
     *
     * @param connectAdbUseCase Use case that validates shell connectivity.
     * @param schedule Block that enqueues the foreground work.
     * @return [Resource.Success] when scheduling succeeds, or [Resource.Error] if connectivity fails.
     */
    suspend inline fun ensureConnectedThenSchedule(
        connectAdbUseCase: ConnectAdbUseCase,
        crossinline schedule: () -> Resource<Unit>
    ): Resource<Unit> {
        return when (val connection = connectAdbUseCase()) {
            is Resource.Success -> schedule()
            is Resource.Error -> connection
        }
    }

    /**
     * Cancels WorkManager-backed work first, then requests repository-side cancellation.
     *
     * Business purpose:
     * - Ensures stop semantics remain consistent across Analysis and Optimization.
     * - Prevents subtle drift where one stop path cancels only WorkManager or only repository.
     *
     * @param cancelWork Block that cancels foreground work.
     * @param cancelRepository Block that cancels repository-side processing.
     * @return The result of repository cancellation (used for UI feedback).
     */
    suspend inline fun cancelWorkThenRepository(
        crossinline cancelWork: () -> Unit,
        crossinline cancelRepository: suspend () -> Resource<Unit>
    ): Resource<Unit> {
        cancelWork()
        return cancelRepository()
    }
}
