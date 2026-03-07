package com.tony.appbooster.presentation.screen.shizuku.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.shape.RoundedCornerShape

/**
 * Reusable action card that drives each step in the Shizuku setup wizard.
 *
 * All wizard steps that require a primary CTA (install, grant, etc.) share
 * this single composable, eliminating structural duplication. Visual differences
 * are expressed solely through the parameters rather than separate composables.
 *
 * @param icon Icon shown in the step header badge.
 * @param title Headline for the step.
 * @param description Body text explaining what the user must do.
 * @param actionLabel Label for the primary action button.
 * @param onActionClicked Callback when the primary button is tapped.
 * @param isLoading When true the primary button is disabled and shows a spinner.
 * @param secondaryActionLabel Optional label for a secondary outlined button.
 * @param onSecondaryActionClicked Callback for the secondary button; required when
 *   [secondaryActionLabel] is non-null.
 */
@Composable
fun SetupStepCard(
    icon: ImageVector,
    title: String,
    description: String,
    actionLabel: String,
    onActionClicked: () -> Unit,
    isLoading: Boolean = false,
    secondaryActionLabel: String? = null,
    onSecondaryActionClicked: (() -> Unit)? = null
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (secondaryActionLabel != null && onSecondaryActionClicked != null) {
                    OutlinedButton(
                        onClick = onSecondaryActionClicked,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(secondaryActionLabel)
                    }
                }

                Button(
                    onClick = onActionClicked,
                    modifier = if (secondaryActionLabel != null) Modifier.weight(1f) else Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(actionLabel)
                    if (!isLoading) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

