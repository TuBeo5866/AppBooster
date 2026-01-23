
package com.tony.appbooster.data.client

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

private const val FALLBACK_ADB_HOST = "127.0.0.1"
private const val FALLBACK_ADB_PORT = 5037

/**
 * Provides a snapshot of the ADB connection configuration derived from
 * persisted user settings so that the DI graph can construct a configured
 * shell client without hardcoded network parameters.
 *
 * The business purpose is to centralize ADB host/port resolution, allowing
 * users to adjust the wireless debugging endpoint while keeping the client
 * creation logic simple for Hilt modules.
 *
 * @param settingsRepository Repository exposing stored ADB connection settings.
 */
@Singleton
class AdbConnectionConfigProvider @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    /**
     * Resolves the effective host and port for the ADB server by reading the
     * latest values from the settings repository. If the lookup fails or
     * returns an error, safe fallback constants are used instead.
     *
     * The business purpose is to give DI modules a synchronous way to obtain
     * a consistent connection configuration derived from asynchronous storage.
     *
     * @return Pair where `first` is the ADB host and `second` is the ADB port.
     */
    fun resolveConfig(): Pair<String, Int> = runBlocking {
        val host = settingsRepository.observeAdbHost()
            .firstOrNullResourceSuccessOrNull()
            ?: FALLBACK_ADB_HOST

        val port = settingsRepository.observeAdbPort()
            .firstOrNullResourceSuccessOrNull()
            ?: FALLBACK_ADB_PORT

        host to port
    }

    /**
     * Awaits the first emission of a `Flow<Resource<T>>` and extracts the
     * underlying value when the emission represents a success, or returns
     * `null` when the flow completes without a value or emits an error.
     *
     * The business purpose is to bridge the asynchronous settings flow with
     * the synchronous configuration needs of DI providers.
     *
     * @param T Type of the wrapped payload.
     * @return The successful payload or `null` if no success was emitted.
     */
    private suspend fun <T> Flow<Resource<T>>.firstOrNullResourceSuccessOrNull(): T? {
        return when (val first = firstOrNull()) {
            is Resource.Success -> first.data
            is Resource.Error -> null
            else -> null
        }
    }
}
