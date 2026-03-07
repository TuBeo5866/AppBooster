package com.tony.appbooster.presentation.navigation

import androidx.navigation.NavOptions
import com.tony.appbooster.presentation.navigation.interfaces.NavigationManager
import com.tony.appbooster.presentation.navigation.interfaces.NavigationCommand
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Default implementation of [NavigationManager].
 *
 * This class uses a [MutableSharedFlow] to emit navigation commands and a
 * [MutableStateFlow] to track the current route, preventing duplicate navigation events.
 * It is provided as a singleton to ensure a single, consistent source of navigation
 * events throughout the app.
 */
@Singleton
class NavigationManagerImpl @Inject constructor() : NavigationManager {

    private val _navigationCommands = MutableSharedFlow<NavigationCommand>(extraBufferCapacity = 1)
    override val navigationCommands = _navigationCommands.asSharedFlow()

    private val _currentRoute = MutableStateFlow<String?>(null)
    override val currentRoute = _currentRoute.asStateFlow()

    override fun navigate(route: String, options: NavOptions?) {
        if (route == _currentRoute.value) return
        _currentRoute.value = route
        _navigationCommands.tryEmit(NavigationCommand.Navigate(route, options))
    }

    override fun navigateUp() {
        _navigationCommands.tryEmit(NavigationCommand.NavigateUp)
    }

    override fun popBackStack(route: String?, inclusive: Boolean) {
        _navigationCommands.tryEmit(NavigationCommand.PopBackStack(route, inclusive))
    }

    override fun navigateAndClearBackStack(route: String, popUpToRoute: String?, inclusive: Boolean) {
        if (route == _currentRoute.value) return
        _currentRoute.value = route
        _navigationCommands.tryEmit(
            NavigationCommand.NavigateAndClearBackStack(
                route,
                popUpToRoute,
                inclusive
            )
        )
    }

    /**
     * Synchronizes the manager's state with the NavController's state.
     * This must be called by the UI host whenever the NavController's destination changes.
     *
     * @param route The new current route from the NavController.
     */
    override fun onRouteChanged(route: String?) {
        _currentRoute.value = route
    }
}