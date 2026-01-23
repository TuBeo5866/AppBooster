package com.tony.appbooster.domain.repository

sealed interface AdbConnectionState {
    data object Disconnected : AdbConnectionState
    data object SearchingPort : AdbConnectionState
    data object Connecting : AdbConnectionState
    data object Connected : AdbConnectionState
    data class Error(val message: String) : AdbConnectionState
}