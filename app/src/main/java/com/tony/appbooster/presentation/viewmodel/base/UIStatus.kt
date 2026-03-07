package com.tony.appbooster.presentation.viewmodel.base

/**
 * Enumerates the lifecycle phases a screen can be in during data loading.
 *
 * Drives the `AppBaseScreen` composable to show loading, content, or error overlays.
 */
enum class UIStatus {
    /** No operation in progress; initial resting state. */
    IDLE,
    /** A background operation is running; show a loading indicator. */
    LOADING,
    /** The operation completed successfully; data is available. */
    SUCCESS,
    /** The operation failed; an error should be surfaced to the user. */
    ERROR
}

