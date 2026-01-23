package com.tony.appbooster.presentation.navigation

sealed class MainNavigationGraph(val route: String) {

    data object MainFlowNode : MainNavigationGraph("main_flow_node")

    data object ShizukuSetupScreen : MainNavigationGraph("shizuku_setup_screen")
    data object SetupScreen : MainNavigationGraph("setup_screen")
    data object SettingsScreen : MainNavigationGraph("settings_screen")
    data object HomeScreen : MainNavigationGraph("home_screen")
}