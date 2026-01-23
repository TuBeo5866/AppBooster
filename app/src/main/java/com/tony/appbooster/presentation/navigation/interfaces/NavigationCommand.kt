package com.example.schedapp.presentation.navigation.interfaces

import androidx.navigation.NavOptions

/**
 * Sealed interface representing all possible navigation commands in the app.
 */
sealed interface NavigationCommand {
    data class Navigate(val route: String, val options: NavOptions? = null) : NavigationCommand
    data object NavigateUp : NavigationCommand
    data class PopBackStack(val route: String?, val inclusive: Boolean) : NavigationCommand
    data class NavigateAndClearBackStack(
        val route: String,
        val popUpToRoute: String?,
        val inclusive: Boolean
    ) : NavigationCommand
}