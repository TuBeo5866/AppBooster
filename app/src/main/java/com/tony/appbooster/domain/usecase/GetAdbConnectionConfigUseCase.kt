package com.tony.appbooster.domain.usecase

import com.tony.appbooster.domain.model.adb.AdbConnectionConfig
import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.common.ResourceError
import com.tony.appbooster.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Provides a reactive ADB connection configuration by merging host and port
 * settings into a single domain model used for debugging and optimization flows.
 *
 * @param settingsRepository Repository that exposes persisted ADB host and port settings.
 * @return A [Flow] emitting [Resource]\<[AdbConnectionConfig]\> describing the current
 * ADB configuration or a typed error when resolution fails.
 */
class GetAdbConnectionConfigUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Observes host and port changes and emits a combined [AdbConnectionConfig] when
     * both sources succeed, or a [Resource.Error] when any source fails.
     *
     * @return A cold [Flow] that emits successful ADB configuration snapshots or
     * propagates the first encountered [ResourceError].
     */
    operator fun invoke(): Flow<Resource<AdbConnectionConfig>> {
        return combine(
            settingsRepository.observeAdbHost(),
            settingsRepository.observeAdbPort(),
            settingsRepository.observeAdbPairingCode()
        ) { hostResult, portResult, paringCode ->
            when {
                hostResult is Resource.Error -> {
                    Resource.Error(hostResult.data)
                }

                portResult is Resource.Error -> {
                    Resource.Error(portResult.data)
                }

                hostResult is Resource.Success &&
                        portResult is Resource.Success &&
                        paringCode is Resource.Success -> {
                    Resource.Success(
                        AdbConnectionConfig(
                            host = hostResult.data,
                            port = portResult.data,
                            pairingCode = paringCode.data
                        )
                    )
                }

                else -> {
                    val error: ResourceError = ResourceError.UnknownError
                    Resource.Error(error)
                }
            }
        }
    }
}
