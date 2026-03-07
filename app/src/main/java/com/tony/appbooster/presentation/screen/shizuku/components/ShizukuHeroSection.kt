package com.tony.appbooster.presentation.screen.shizuku.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tony.appbooster.R
import com.tony.appbooster.presentation.viewmodel.shizuku.ShizukuSetupStep
import androidx.compose.material3.Icon

/**
 * Hero section displayed at the top of the Shizuku setup screen.
 *
 * Animates its icon, title, and subtitle as [step] changes, using expressive
 * M3 spring transitions to communicate progress to the user.
 *
 * @param step Current wizard step driving the hero content.
 */
@Composable
fun ShizukuHeroSection(step: ShizukuSetupStep) {
    val icon: ImageVector = when (step) {
        ShizukuSetupStep.CHECK_STATUS -> Icons.Rounded.Terminal
        ShizukuSetupStep.INSTALL_SHIZUKU -> Icons.Rounded.Download
        ShizukuSetupStep.START_SERVICE -> Icons.Rounded.PlayArrow
        ShizukuSetupStep.GRANT_PERMISSION -> Icons.Rounded.Key
        ShizukuSetupStep.READY -> Icons.Rounded.CheckCircle
    }

    val title = when (step) {
        ShizukuSetupStep.CHECK_STATUS -> stringResource(R.string.shizuku_hero_checking_title)
        ShizukuSetupStep.INSTALL_SHIZUKU -> stringResource(R.string.shizuku_hero_install_title)
        ShizukuSetupStep.START_SERVICE -> stringResource(R.string.shizuku_hero_start_title)
        ShizukuSetupStep.GRANT_PERMISSION -> stringResource(R.string.shizuku_hero_permission_title)
        ShizukuSetupStep.READY -> stringResource(R.string.shizuku_hero_ready_title)
    }

    val subtitle = when (step) {
        ShizukuSetupStep.CHECK_STATUS -> stringResource(R.string.shizuku_hero_checking_subtitle)
        ShizukuSetupStep.INSTALL_SHIZUKU -> stringResource(R.string.shizuku_hero_install_subtitle)
        ShizukuSetupStep.START_SERVICE -> stringResource(R.string.shizuku_hero_start_subtitle)
        ShizukuSetupStep.GRANT_PERMISSION -> stringResource(R.string.shizuku_hero_permission_subtitle)
        ShizukuSetupStep.READY -> stringResource(R.string.shizuku_hero_ready_subtitle)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = step,
            transitionSpec = {
                (scaleIn(spring(stiffness = Spring.StiffnessLow)) + fadeIn()) togetherWith
                        (scaleOut() + fadeOut())
            },
            label = "hero_icon"
        ) { currentStep ->
            val isReady = currentStep == ShizukuSetupStep.READY
            val containerColor = if (isReady) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.tertiaryContainer
            val contentColor = if (isReady) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onTertiaryContainer

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = contentColor
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedContent(
            targetState = title,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "hero_title"
        ) { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedContent(
            targetState = subtitle,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "hero_subtitle"
        ) { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

