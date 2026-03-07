package com.tony.appbooster.presentation.viewmodel.base

/**
 * Holds human-readable error information to be surfaced in the UI layer.
 *
 * Instances are attached to [UIState.error] when [UIStatus.ERROR] is active and
 * displayed via the [BaseDialog] in [AppBaseScreen].
 *
 * @property title Short, user-facing error headline (e.g., "Connection failed").
 * @property message Detailed description of the error suitable for end users.
 * @property type Optional discriminator that the UI layer can use to customize
 *   the error presentation (e.g., map to a specific icon or string resource).
 * @property retryAction Optional callback invoked when the user taps the retry
 *   button in the error dialog. When null the retry button is hidden.
 */
data class UIError(
    val title: String,
    val message: String,
    val type: Any? = null,
    val retryAction: (() -> Unit)? = null
)

