package com.tony.appbooster.domain.usecase

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Provides a reactive stream of the effective ADB host so presentation
 * components can react to changes without querying storage directly.
 *
 * @param settingsRepository Repository exposing the stored ADB host.
 * @return A [Flow] of [Resource] values wrapping the current ADB host string.
 */
class ObserveAdbHostUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Starts observing ADB host changes from the settings layer and forwards
     * the results as-is to the caller.
     *
     * @return A cold [Flow] emitting [Resource.Success] with the host or
     * [Resource.Error] if the value cannot be read.
     */
    operator fun invoke(): Flow<Resource<String>> = settingsRepository.observeAdbHost()
}