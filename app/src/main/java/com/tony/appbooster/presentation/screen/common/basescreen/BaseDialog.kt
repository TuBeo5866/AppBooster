package com.tony.appbooster.presentation.screen.common.basescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tony.appbooster.R
import com.tony.appbooster.presentation.tools.AppBasePreview
import com.tony.appbooster.presentation.ui.theme.paddingLarge
import com.tony.appbooster.presentation.ui.theme.paddingMedium
import com.tony.appbooster.presentation.ui.theme.paddingSSmall
import com.tony.appbooster.presentation.ui.theme.paddingSmall
import com.tony.appbooster.presentation.ui.theme.paddingXLarge

/**
 *
 * @param onDismissRequest Lambda invoked when the dialog is dismissed (e.g., by back press or scrim click).
 * @param icon Optional icon to display at the top of the dialog.
 * @param title The title of the dialog.
 * @param message The main content/message of the dialog.
 * @param confirmButtonText Text for the confirm button.
 * @param onConfirm Lambda invoked when the confirm button is clicked.
 * @param dismissButtonText Optional text for the dismiss button. If null, the button is not shown.
 * @param onCancel Lambda invoked when the dismiss button is clicked.
 * @param properties Optional DialogProperties to customize the dialog's behavior.
 */
@Composable
fun BaseDialog(
    onDismissRequest: () -> Unit,
    icon: ImageVector? = null,
    title: String,
    message: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    dismissButtonText: String? = null,
    onCancel: (() -> Unit)? = null,
    retryButtonText: String? = null,
    onRetry: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties()
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingMedium),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = paddingSSmall)
        ) {
            Column(
                modifier = Modifier.padding(paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(paddingMedium)
            ) {
                // Optional Icon
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(paddingXLarge)
                    )
                }

                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                // Message
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = paddingSSmall)
                )

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(paddingSmall, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dismiss Button (Optional)
                    if (dismissButtonText != null && onCancel != null) {
                        TextButton(
                            onClick = {
                                onCancel()
                                onDismissRequest()
                            }
                        ) {
                            Text(text = dismissButtonText)
                        }
                    }

                    if (retryButtonText != null && onRetry != null) {
                        TextButton(
                            onClick = {
                                onRetry()
                                onDismissRequest()
                            }
                        ) {
                            Text(text = retryButtonText)
                        }
                    }

                    FilledTonalButton(
                        onClick = {
                            onConfirm()
                            onDismissRequest()
                        }
                    ) {
                        Text(text = confirmButtonText)
                    }
                }
            }
        }
    }
}

@Composable
@AppBasePreview
fun BaseDialogPreview(){
    BaseDialog(
        onDismissRequest = { },
        icon = Icons.Filled.Close,
        title = stringResource(R.string.base_dialog_preview_title),
        message = stringResource(R.string.base_dialog_preview_message),
        confirmButtonText = stringResource(R.string.base_dialog_preview_confirm),
        onConfirm = {},
        dismissButtonText = stringResource(R.string.base_dialog_preview_cancel),
        onCancel = {},
        retryButtonText = stringResource(R.string.action_retry),
        onRetry = {  }
    )
}