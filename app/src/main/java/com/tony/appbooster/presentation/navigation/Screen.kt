package com.tony.appbooster.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object ShizukuSetup : Screen("shizuku_setup", "Shizuku Setup", Icons.Rounded.Security)
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Rounded.Build)
    data object Settings : Screen("settings", "Settings", Icons.Rounded.Settings)
}