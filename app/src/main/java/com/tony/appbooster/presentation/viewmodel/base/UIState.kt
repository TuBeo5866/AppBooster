package com.example.schedapp.presentation.viewmodel.base

/**
 * A sealed class representing the UI state of a view.  This allows for a consistent and
 * exhaustive handling of loading, success, and error states.
 *
 * @param <T> The type of data held by the Success state.
 */

enum class UIStatus{ LOADING, ERROR, SUCCESS, IDLE}

enum class LoginState{ DEFAULT, LOGGED_IN, LOGGED_OUT, ERROR, SESSION_EXPIRED}

data class UIError(
    val title: String,
    val message: String,
    val type: Any? = null,
    val retryAction: (() -> Unit)? = null
)

data class UIState<T>(
    val status : UIStatus = UIStatus.IDLE,
    val data : T? = null,
    val error : UIError? = null,
    val loginStatus : LoginState = LoginState.DEFAULT,
    var showErrorDialog: Boolean = false
)