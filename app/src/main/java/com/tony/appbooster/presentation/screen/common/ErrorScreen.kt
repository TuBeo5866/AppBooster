package com.tony.appbooster.presentation.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tony.appbooster.R

/**
 * A generic screen for displaying errors or informational messages.
 * It shows an image, a title, a description, and up to three optional buttons.
 *
 * @param image The vector graphic to display at the top of the screen.
 * @param title The main message or title of the error screen.
 * @param description A more detailed explanation of the state.
 * @param primaryButtonText The text for the main action button. The button is only shown if this is not null/empty and [onPrimaryButtonClick] is not null.
 * @param onPrimaryButtonClick The callback to be invoked when the primary button is clicked.
 * @param secondaryButtonText The text for the secondary action button. The button is only shown if this is not null/empty and [onSecondaryButtonClick] is not null.
 * @param onSecondaryButtonClick The callback to be invoked when the secondary button is clicked.
 * @param tertiaryButtonText The text for the tertiary action button. The button is only shown if this is not null/empty and [onTertiaryButtonClick] is not null.
 * @param onTertiaryButtonClick The callback to be invoked when the tertiary button is clicked.
 */
@Composable
fun ErrorScreen(
    image: ImageVector = ImageVector.vectorResource(R.drawable.ic_close),
    title: String,
    description: String,
    primaryButtonText: String? = null,
    onPrimaryButtonClick: (() -> Unit)? = null,
    secondaryButtonText: String? = null,
    onSecondaryButtonClick: (() -> Unit)? = null,
    tertiaryButtonText: String? = null,
    onTertiaryButtonClick: (() -> Unit)? = null,
) {

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = image,
                contentDescription = stringResource(R.string.error_icon_cd),
                modifier = Modifier.size(64.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!primaryButtonText.isNullOrEmpty() && onPrimaryButtonClick != null) {
                Button(
                    onClick = onPrimaryButtonClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(text = primaryButtonText)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (!secondaryButtonText.isNullOrEmpty() && onSecondaryButtonClick != null) {
                Button(
                    onClick = onSecondaryButtonClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(text = secondaryButtonText)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (!tertiaryButtonText.isNullOrEmpty() && onTertiaryButtonClick != null) {
                Button(
                    onClick = onTertiaryButtonClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    Text(text = tertiaryButtonText)
                }
            }
        }
    }
}
