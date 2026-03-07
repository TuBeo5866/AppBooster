package com.tony.appbooster.presentation.viewmodel.base

/**
 * Represents the user's current authentication / session phase.
 *
 * Used by [UIState] to allow ViewModels to respond to session lifecycle
 * events without coupling navigation to individual screens.
 */
enum class LoginState {
    /** No session check has been performed yet; the default resting state. */
    DEFAULT,
    /** A valid session is active and the user is authenticated. */
    LOGGED_IN,
    /** The session was explicitly terminated by the user or the server. */
    LOGGED_OUT,
    /** Authentication failed due to invalid credentials or a network issue. */
    ERROR,
    /** A previously valid session has expired and reauthentication is required. */
    SESSION_EXPIRED
}

