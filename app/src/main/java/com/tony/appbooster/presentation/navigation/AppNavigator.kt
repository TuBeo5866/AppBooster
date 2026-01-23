package com.tony.appbooster.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alkemy.boxapp.presentation.navigation.interfaces.NavigationManager
import com.example.schedapp.presentation.navigation.interfaces.NavigationCommand

/**
 * A composable that manages the app's navigation.
 *
 * This component listens for navigation commands from a [NavigationManager] and uses a
 * [androidx.navigation.NavController] to perform the navigation actions. It sets up the main navigation host
 * for the application.
 *
 * @param navigationManager The manager that provides navigation commands.
 */
@Composable
fun AppNavigator(
    navigationManager: NavigationManager
) {
    val navController = rememberNavController()

    // Observe NavController's back stack and sync with NavigationManager
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        currentRoute?.let {
            navigationManager.onRouteChanged(it)
        }
    }

    LaunchedEffect(Unit) {
        navigationManager.navigationCommands.collect { command ->
            when (command) {
                is NavigationCommand.Navigate -> {
                    navController.navigate(command.route, command.options)
                }
                is NavigationCommand.NavigateUp -> {
                    navController.navigateUp()
                }
                is NavigationCommand.PopBackStack -> {
                    if (command.route != null) {
                        navController.popBackStack(command.route, command.inclusive)
                    } else {
                        navController.popBackStack()
                    }
                }
                is NavigationCommand.NavigateAndClearBackStack -> {
                    navController.navigate(command.route) {
                        command.popUpToRoute?.let { popUpTo(it) { inclusive = command.inclusive } }
                            ?: popUpTo(navController.graph.startDestinationId) { inclusive = command.inclusive }
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    AppBoosterNavigationGraph(
        navController = navController
    )
}
