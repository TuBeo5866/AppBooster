package com.tony.appbooster.presentation.screen.settings.model

/**
 * Represents static application metadata used for displaying build information
 * to the user within the Settings screen.
 *
 * @param versionName Human-readable application version name (e.g. 1.0.0).
 * @param buildChannel Optional build channel label (e.g. Alpha, Beta, Release).
 * @return Domain entity carrying app identification data.
 */
data class AppInfo(
    val versionName: String,
    val buildChannel: String?
)
