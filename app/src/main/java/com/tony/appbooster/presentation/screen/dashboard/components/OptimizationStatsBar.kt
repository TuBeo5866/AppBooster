package com.tony.appbooster.presentation.screen.dashboard.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Horizontal stats bar summarising optimization results in three tonal chips.
 *
 * Displays optimized, skipped, and (conditionally) failed counts with animated
 * value transitions driven by a spring spec for a snappy, tactile feel.
 *
 * @param optimizedCount Number of apps successfully optimized.
 * @param skippedCount Number of apps skipped during the run.
 * @param failedCount Number of apps that failed; the error chip is hidden when zero.
 * @param modifier Modifier for layout customization.
 */
@Composable
fun OptimizationStatsBar(
    optimizedCount: Int,
    skippedCount: Int,
    failedCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatChip(
            count = optimizedCount,
            color = MaterialTheme.colorScheme.primary,
            icon = Icons.Rounded.CheckCircle,
            modifier = Modifier.weight(1f)
        )
        StatChip(
            count = skippedCount,
            color = MaterialTheme.colorScheme.tertiary,
            icon = Icons.Rounded.FastForward,
            modifier = Modifier.weight(1f)
        )
        if (failedCount > 0) {
            StatChip(
                count = failedCount,
                color = MaterialTheme.colorScheme.error,
                icon = Icons.Rounded.Error,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Individual stat chip displaying an icon and an animated count.
 *
 * @param count Value to display; animates with a spring when it changes.
 * @param color Tonal color used for the icon and text.
 * @param icon Icon representing the stat category.
 * @param modifier Modifier for layout customization.
 */
@Composable
private fun StatChip(
    count: Int,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val animatedCount by animateFloatAsState(
        targetValue = count.toFloat(),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "countAnimation"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = animatedCount.toInt().toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

