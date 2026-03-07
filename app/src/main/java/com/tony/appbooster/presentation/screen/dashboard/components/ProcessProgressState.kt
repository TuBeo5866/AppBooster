package com.tony.appbooster.presentation.screen.dashboard.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PersonOff
import androidx.compose.material.icons.rounded.RocketLaunch
import com.tony.appbooster.domain.model.common.OptimizationAnalysis
import com.tony.appbooster.domain.model.common.OptimizationProgress

/**
 * Represents the two in-progress states that drive [ProcessProgressContent]:
 * an active optimization run and an active analysis scan.
 *
 * Encapsulating each variant here keeps [ProcessProgressContent] free from
 * domain types and raw string interpolation, and removes the need for a
 * separate [ScanningContent] wrapper composable.
 *
 * @property title Primary headline displayed at the top of the progress card.
 * @property subtitle Secondary line showing e.g. "10 / 50 apps".
 * @property progress Fractional progress from 0f to 1f.
 * @property currentPackage Package name currently being processed, empty if none.
 * @property statChips Typed chip list for the live stats row; empty hides the row.
 */
sealed interface ProcessProgressState {

    val title: String
    val subtitle: String
    val progress: Float
    val currentPackage: String
    val statChips: List<ProcessStatChip>

    /**
     * An optimization run is actively processing apps.
     *
     * @property title Headline for the optimization phase.
     * @property subtitle Fractional count label, e.g. "12 / 45 apps".
     * @property progress Fractional progress from 0f to 1f.
     * @property currentPackage Package currently being compiled.
     * @property statChips Always empty for optimization – no live stats shown mid-run.
     */
    data class Optimizing(
        override val title: String,
        override val subtitle: String,
        override val progress: Float,
        override val currentPackage: String,
        override val statChips: List<ProcessStatChip> = emptyList()
    ) : ProcessProgressState

    /**
     * An analysis scan is actively inspecting installed apps.
     *
     * Chip list is built from the [OptimizationAnalysis] passed to
     * [ProcessProgressState.fromOptimizationAnalysis] so callers never construct chips manually.
     *
     * @property title Headline for the scanning phase.
     * @property subtitle Fractional count label, or a generic "checking…" string.
     * @property progress Fractional progress from 0f to 1f.
     * @property currentPackage Package currently being inspected.
     * @property statChips Live breakdown chips; empty until the first app is scanned.
     */
    data class Scanning(
        override val title: String,
        override val subtitle: String,
        override val progress: Float,
        override val currentPackage: String,
        override val statChips: List<ProcessStatChip>
    ) : ProcessProgressState

    companion object {
        /**
         * Constructs an [Optimizing] state from a domain [OptimizationProgress].
         *
         * @param progress Live optimization progress from the domain layer.
         * @param titleText Localised headline string.
         * @return [Optimizing] state ready for [ProcessProgressContent].
         */
        fun fromOptimizationProgress(
            progress: OptimizationProgress,
            titleText: String
        ): Optimizing = Optimizing(
            title = titleText,
            subtitle = "${progress.processedCount} / ${progress.totalCount} apps",
            progress = progress.progress,
            currentPackage = progress.currentAppPackage
        )

        /**
         * Constructs a [Scanning] state from a domain [OptimizationAnalysis].
         *
         * Builds the live stat chips from analysis counters so the composable
         * layer never touches domain models directly.
         *
         * @param analysis Live analysis state from the domain layer.
         * @param titleText Localised headline string.
         * @param subtitleText Localised subtitle when total app count is unknown.
         * @return [Scanning] state ready for [ProcessProgressContent].
         */
        fun fromOptimizationAnalysis(
            analysis: OptimizationAnalysis,
            titleText: String,
            subtitleText: String
        ): Scanning {
            val chips = if (analysis.totalAppsScanned > 0) {
                buildList {
                    add(
                        ProcessStatChip(
                            count = analysis.appsNeedingOptimization,
                            label = "need",
                            icon = Icons.Rounded.RocketLaunch,
                            style = ProcessStatChipStyle.Pending
                        )
                    )
                    // Show no-profile chip only when there are matching apps
                    if (analysis.appsWithNoProfile > 0) {
                        add(
                            ProcessStatChip(
                                count = analysis.appsWithNoProfile,
                                label = "no profile",
                                icon = Icons.Rounded.PersonOff,
                                style = ProcessStatChipStyle.Neutral
                            )
                        )
                    }
                    add(
                        ProcessStatChip(
                            count = analysis.appsAlreadyOptimized,
                            label = "done",
                            icon = Icons.Rounded.CheckCircle,
                            style = ProcessStatChipStyle.Done
                        )
                    )
                }
            } else {
                emptyList()
            }

            return Scanning(
                title = titleText,
                subtitle = if (analysis.totalAppsToScan > 0)
                    "${analysis.totalAppsScanned} / ${analysis.totalAppsToScan} apps"
                else
                    subtitleText,
                progress = analysis.progress,
                currentPackage = analysis.currentPackage,
                statChips = chips
            )
        }
    }
}

