package com.tony.appbooster.presentation.screen.dashboard.components

/**
 * Describes a single stat row entry rendered inside an [OptimizationStatsRow].
 *
 * Each instance maps one statistical counter to its label and visual colour role
 * so the composable can iterate over a list instead of hard-coding separate rows.
 *
 * @property count Numerical value to display.
 * @property label Short descriptive label shown next to the count.
 * @property style Colour role applied to the leading dot indicator.
 */
data class ProcessStatChip(
    val count: Int,
    val label: String,
    val style: ProcessStatChipStyle
)

/**
 * Colour role variants for a [ProcessStatChip].
 *
 * Each variant maps to a distinct M3 colour token so dot colour is driven
 * by data rather than branching inside the composable.
 */
enum class ProcessStatChipStyle {
    /** Primary colour – highlights items that still need work. */
    Pending,

    /** Muted surface-variant – neutral, for informational counts. */
    Neutral,

    /** Tertiary colour – positive, for completed counts. */
    Done
}
