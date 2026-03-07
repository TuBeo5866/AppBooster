package com.tony.appbooster.presentation.screen.shizuku.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tony.appbooster.R
import com.tony.appbooster.presentation.viewmodel.shizuku.ShizukuSetupStep

/**
 * Animated linear progress bar showing how far through the Shizuku setup wizard the user is.
 *
 * Progress animates with a low-stiffness spring whenever [step] changes to provide
 * a satisfying visual advancement between wizard stages.
 *
 * @param step The current wizard step used to derive the progress fraction.
 */
@Composable
fun SetupProgressIndicator(step: ShizukuSetupStep) {
    val progress by animateFloatAsState(
        targetValue = when (step) {
            ShizukuSetupStep.CHECK_STATUS -> 0f
            ShizukuSetupStep.INSTALL_SHIZUKU -> 0.25f
            ShizukuSetupStep.START_SERVICE -> 0.5f
            ShizukuSetupStep.GRANT_PERMISSION -> 0.75f
            ShizukuSetupStep.READY -> 1f
        },
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "setup_progress"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.shizuku_setup_progress_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            strokeCap = StrokeCap.Round,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    }
}

