package com.tony.appbooster.presentation.screen.common.basescreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.example.schedapp.presentation.screen.common.basescreen.ErrorDialogConfig
import com.example.schedapp.presentation.viewmodel.base.UIState
import com.example.schedapp.presentation.viewmodel.base.UIStatus
import com.tony.appbooster.R
import com.tony.appbooster.presentation.screen.common.LoadingScreen

/**
 * A base screen composable that handles different UI states like Loading, Success, and Error.
 * It provides a consistent structure for screens across the app, managing animations,
 * dialogs, and system UI appearance.
 *
 * @param T The type of the data held by the [UIState].
 * @param uiState The state object that drives the UI, containing status, data, and error information.
 * @param statusBarColor The color for the system status bar. Defaults to the `surface` color of the current [MaterialTheme].
 * @param useLightStatusIcons Determines if the status bar icons should be light. If null, an optimal value is chosen based on the luminance of [statusBarColor].
 * @param errorDialogConfig Configuration for the error dialog.
 * @param loadingType Defines the behavior of the loading indicator (e.g., fullscreen, overlay).
 * @param loadingScreen A custom composable to be displayed during the loading state. Overrides the default loading indicator.
 * @param errorScreen A custom composable to be displayed during the error state. Overrides the default error dialog.
 * @param content The main content of the screen to be displayed when the state is [UIStatus.SUCCESS] or when data is available in other states.
 */
@Composable
fun <T> AppBaseScreen(
    uiState: UIState<T>,
    statusBarColor: Color = MaterialTheme.colorScheme.surface,
    useLightStatusIcons: Boolean? = null,
    errorDialogConfig: ErrorDialogConfig = ErrorDialogConfig(),
    loadingType: BaseLoadingType = BaseLoadingType.DEFAULT,
    loadingScreen: (@Composable () -> Unit)? = null,
    errorScreen: (@Composable () -> Unit)? = null,
    content: @Composable (T) -> Unit
) {
    SystemAppearance(
        statusBarColor = statusBarColor,
        useLightStatusIcons = useLightStatusIcons
    )

    AnimatedContent(
        targetState = uiState,
        label = "AppBaseScreenAnimation",
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { targetState ->
        Box {
            when (targetState.status) {
                UIStatus.LOADING -> {
                    if (loadingType != BaseLoadingType.DEFAULT) {
                        targetState.data?.let { content(it) }
                    }
                    if (loadingScreen != null) {
                        loadingScreen()
                    } else {
                        when (loadingType) {
                            BaseLoadingType.DEFAULT -> LoadingScreen()
                            BaseLoadingType.OVERLAY -> LoadingScreen(
                                backgroundColor = Color.Gray.copy(
                                    alpha = 0.5f
                                )
                            )

                            BaseLoadingType.NONE -> { /* No loading indicator */
                            }
                        }
                    }
                }

                UIStatus.SUCCESS, UIStatus.IDLE -> {
                    targetState.data?.let { content(it) }
                }

                UIStatus.ERROR -> {
                    targetState.data?.let { content(it) }
                    if (errorScreen != null) {
                        errorScreen()
                    } else if (targetState.showErrorDialog && targetState.error != null) {
                        BaseDialog(
                            title = targetState.error.title,
                            message = targetState.error.message,
                            confirmButtonText = errorDialogConfig.confirmButtonText
                                ?: stringResource(R.string.ok),
                            retryButtonText = errorDialogConfig.retryButtonText,
                            dismissButtonText = errorDialogConfig.dismissButtonText,
                            onConfirm = errorDialogConfig.onConfirm,
                            onRetry = targetState.error.retryAction,
                            onCancel = errorDialogConfig.onCancel,
                            onDismissRequest = { errorDialogConfig.onDismissRequest?.invoke() },
                            properties = DialogProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false
                            )
                        )
                    }
                }
            }
        }
    }
}
