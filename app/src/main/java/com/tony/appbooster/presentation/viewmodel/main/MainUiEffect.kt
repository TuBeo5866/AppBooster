package com.tony.appbooster.presentation.viewmodel.main

/**
 * One-shot UI effects for the Dashboard/Main screen.
 *
 * Effects are transient and must not be used for persistent rendering state.
 * Typical examples are snackbars, toasts, or navigation prompts.
 */
sealed interface MainUiEffect {

    /**
     * Requests showing a brief message to the user, typically via Snackbar.
     *
     * @property message Human-readable message to show.
     */
    data class ShowSnackbar(val message: String) : MainUiEffect
}
