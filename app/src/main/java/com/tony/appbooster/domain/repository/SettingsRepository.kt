package com.tony.appbooster.domain.repository

import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import kotlinx.coroutines.flow.Flow

/**
 * Provides access to user-facing runtime configuration such as optimization
 * behavior and ADB connection details, exposing typed results wrapped into
 * [Resource] for error-aware handling across the domain layer.
 */
interface SettingsRepository {

    /**
     * Observes the currently selected optimization mode so the UI can react
     * to changes, wrapping every emission into [Resource].
     *
     * @return A cold [Flow] emitting [Resource] instances representing the
     * active [AppOptimizationType].
     */
    fun observeAppOptimizationType(): Flow<Resource<AppOptimizationType>>

    /**
     * Persists the chosen optimization mode to durable storage so it is
     * restored on next launch.
     *
     * @param type The [AppOptimizationType] selected by the user.
     * @return A [Resource] describing the success or error of the operation.
     */
    suspend fun setAppOptimizationType(
        type: AppOptimizationType
    ): Resource<Unit>

    /**
     * Observes the configured ADB host name so that connection components
     * can use user-provided or default values without hardcoding.
     *
     * @return A [Flow] emitting the effective ADB host wrapped in [Resource].
     */
    fun observeAdbHost(): Flow<Resource<String>>

    /**
     * Persists the ADB host name provided by the user for future sessions,
     * enabling dynamic reconfiguration of the ADB client.
     *
     * @param host Hostname or IP address of the ADB server.
     * @return A [Resource] describing whether the host was stored correctly.
     */
    suspend fun setAdbHost(
        host: String
    ): Resource<Unit>

    /**
     * Persists the ADB host name provided by the user for future sessions,
     * enabling dynamic reconfiguration of the ADB client.
     *
     * @param host Hostname or IP address of the ADB server.
     * @return A [Resource] describing whether the host was stored correctly.
     */
    suspend fun setAdbParingCode(
        code: Int
    ): Resource<Unit>

    /**
     * Observes the configured ADB TCP port so that the client can connect
     * without relying on magic numbers or compile-time constants.
     *
     * @return A [Flow] emitting the effective ADB port wrapped in [Resource].
     */
    fun observeAdbPort(): Flow<Resource<Int>>


    /**
     * Observes the configured ADB TCP pairing code client can connect
     * without relying on magic numbers or compile-time constants.
     *
     * @return A [Flow] emitting the effective ADB port wrapped in [Resource].
     */
    fun observeAdbPairingCode(): Flow<Resource<Int>>

    /**
     * Persists the ADB TCP port provided by the user, ensuring the client
     * connects to the correct ADB server instance across app restarts.
     *
     * @param port TCP port number that the ADB server listens on.
     * @return A [Resource] describing whether the port was stored correctly.
     */
    suspend fun setAdbPort(
        port: Int
    ): Resource<Unit>
}
