package com.tony.appbooster.presentation.screen.shizuku.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tony.appbooster.R

/**
 * Step card shown while the app is detecting the current Shizuku state.
 *
 * Renders a spinner while checking and an error message with a retry button
 * if the detection fails.
 *
 * @param isChecking Whether a background status check is in progress.
 * @param error Error message to show if the last check failed; null when no error.
 * @param onRetry Callback invoked when the user taps the retry button.
 */
@Composable
fun CheckingStatusCard(
    isChecking: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isChecking) {
                CircularProgressIndicator()
                Text(
                    text = stringResource(R.string.shizuku_checking_status),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else if (error != null) {
                Text(
                    text = stringResource(R.string.shizuku_checking_error_with_reason, error),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.action_retry))
                }
            }
        }
    }
}

/**
 * Step card directing the user to download and install Shizuku.
 *
 * Delegates rendering to the shared [SetupStepCard] layout.
 *
 * @param onInstallClicked Callback when the download CTA is tapped.
 */
@Composable
fun InstallShizukuCard(onInstallClicked: () -> Unit) {
    SetupStepCard(
        icon = Icons.Rounded.Download,
        title = stringResource(R.string.shizuku_hero_install_title),
        description = stringResource(R.string.shizuku_install_description),
        actionLabel = stringResource(R.string.shizuku_download_action),
        onActionClicked = onInstallClicked
    )
}

/**
 * Step card guiding the user to grant Shizuku permission to this app.
 *
 * Delegates rendering to the shared [SetupStepCard] layout.
 *
 * @param onGrantClicked Callback when the grant-permission CTA is tapped.
 * @param isRequesting Whether the permission request is pending.
 */
@Composable
fun GrantPermissionCard(
    onGrantClicked: () -> Unit,
    isRequesting: Boolean
) {
    SetupStepCard(
        icon = Icons.Rounded.Key,
        title = stringResource(R.string.shizuku_hero_permission_title),
        description = stringResource(R.string.shizuku_grant_description),
        actionLabel = if (isRequesting) {
            stringResource(R.string.shizuku_grant_requesting)
        } else {
            stringResource(R.string.shizuku_hero_permission_title)
        },
        onActionClicked = onGrantClicked,
        isLoading = isRequesting
    )
}

