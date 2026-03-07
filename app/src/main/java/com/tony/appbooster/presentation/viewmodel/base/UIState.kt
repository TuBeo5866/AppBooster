package com.tony.appbooster.presentation.viewmodel.base

/**
 * Immutable representation of a screen's full UI state, combining lifecycle status,
 * domain data, and transient error information into a single value type.
 *
 * @param T The type of domain data surfaced to the UI layer.
 * @property status Current lifecycle phase of the screen (idle, loading, success, or error).
 * @property data Domain data snapshot; non-null when status is [UIStatus.SUCCESS] or [UIStatus.IDLE].
 * @property error Transient error information; non-null when status is [UIStatus.ERROR].
 * @property loginStatus Current session / authentication phase.
 * @property showErrorDialog Whether the error dialog overlay should be displayed.
 */
data class UIState<T>(
    val status: UIStatus = UIStatus.IDLE,
    val data: T? = null,
    val error: UIError? = null,
    val loginStatus: LoginState = LoginState.DEFAULT,
    val showErrorDialog: Boolean = false
)