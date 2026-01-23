package com.tony.appbooster.domain.model.adb

/**
 * Represents the effective ADB connection configuration used by the
 * optimization pipeline to communicate with the host ADB server.
 *
 * @param host Hostname or IP address of the ADB server instance.
 * @param port TCP port that the ADB server listens on.
 * @param pairingCode Optional pairing code, reserved for future use.
 * @return Immutable value object describing the ADB endpoint.
 */
data class AdbConnectionConfig(
    val host: String,
    val port: Int,
    val pairingCode: Int
)
