package com.tony.appbooster.presentation.screen.dashboard.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Resolved visual tokens for a single activity log row.
 *
 * Produced by `resolveLogEntryStyle` so that all colour/icon decisions
 * live in one place rather than being scattered across the composable body.
 *
 * @property icon Icon representing the log entry type.
 * @property color Foreground tint for the icon and text accents.
 * @property backgroundColor Translucent container tint for the row background.
 */
internal data class LogEntryStyle(
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color
)

