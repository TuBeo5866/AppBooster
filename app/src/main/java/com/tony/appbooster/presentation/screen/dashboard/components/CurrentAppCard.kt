package com.tony.appbooster.presentation.screen.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

/**
 * Card highlighting the app currently being optimized.
 *
 * Loads the app's launcher icon and label asynchronously. The card slides in
 * when [packageName] is non-empty and fades out when it becomes empty.
 *
 * @param packageName Package name of the app being optimized; the card is hidden when blank.
 * @param modifier Modifier for layout customization.
 */
@Composable
fun CurrentAppCard(
    packageName: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = packageName.isNotEmpty(),
        enter = fadeIn(tween(200)) + slideInVertically { it / 2 },
        exit = fadeOut(tween(150))
    ) {
        CurrentAppCardContent(packageName = packageName, modifier = modifier)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private helpers
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Stateless content for [CurrentAppCard], isolated to ensure correct recomposition
 * when the package name changes mid-optimization.
 */
@Composable
private fun CurrentAppCardContent(
    packageName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var appIcon by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var appLabel by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(packageName) {
        if (packageName.isNotEmpty()) {
            try {
                val pm = context.packageManager
                val applicationInfo = pm.getApplicationInfo(packageName, 0)
                appIcon = applicationInfo.loadIcon(pm).toBitmap(width = 96, height = 96)
                appLabel = pm.getApplicationLabel(applicationInfo).toString()
            } catch (_: Exception) {
                appIcon = null
                appLabel = null
            }
        } else {
            appIcon = null
            appLabel = null
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App icon with fallback initial
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (appIcon != null) {
                        Image(
                            bitmap = appIcon!!.asImageBitmap(),
                            contentDescription = appLabel,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = packageName.substringAfterLast(".").take(1).uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Optimizing",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = appLabel
                        ?: packageName.substringAfterLast(".").replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

