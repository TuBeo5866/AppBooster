package com.tony.appbooster.presentation.permission

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Explains why notification permission is requested.
 *
 * The business purpose is to build user trust: the optimization runs in the foreground
 * and needs a notification to stay alive in background and to provide a Stop action.
 *
 * @param onConfirm Invoked when the user agrees to grant the permission.
 * @param onDismiss Invoked when the user declines; optimization will continue in-app only.
 */
@Composable
fun NotificationPermissionRationaleDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Enable notifications?",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "OptiDroid uses a foreground notification to keep optimization running in the background and to show a Stop button.\n\nWe only post notifications while optimization is running.",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not now")
            }
        }
    )
}
