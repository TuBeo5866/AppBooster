package com.tony.appbooster.presentation.navigation.interfaces

import androidx.navigation.NavOptions
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for managing navigation throughout the application.
 *
 * This manager decouples navigation logic from ViewModels and UI components,
 * allowing navigation to be triggered from anywhere in the business logic layer.
 * It follows the command pattern, emitting navigation events that a central UI
 * component (like the main activity) can observe and execute.
 */
interface NavigationManager {
    /**
     * A hot flow of navigation commands that must be collected by a UI host
     * (e.g., MainActivity) to perform the actual navigation via a NavController.
     */
    val navigationCommands: SharedFlow<NavigationCommand>

    /**
     * A state flow that holds the current navigation route as a string.
     * This allows components like ViewModels to know the user's current location
     * in the app without direct access to the NavController.
     */
    val currentRoute: StateFlow<String?>

    /**
     * Issues a command to navigate to the specified route.
     *
     * Implementations should be idempotent, meaning calling this multiple times
     * with the same route should not result in multiple navigation actions.
     *
     * @param route The destination route to navigate to (e.g., from a NavGraph).
     * @param options Optional [NavOptions] for customizing navigation behavior,
     * such as animations or pop-up behavior.
     */
    fun navigate(route: String, options: NavOptions? = null)

    /**
     * Issues a command to navigate up in the back stack. This is equivalent to the
     * user pressing the system back button.
     */
    fun navigateUp()

    /**
     * Issues a command to pop entries from the back stack up to a specific destination.
     *
     * @param route The route to pop back to. If null, it pops only the top entry.
     * @param inclusive If true, the specified [route] destination is also popped.
     */
    fun popBackStack(route: String? = null, inclusive: Boolean = false)

    /**
     * Issues a command to navigate to a new route while clearing a portion of the
     * back stack. This is useful for navigation flows where going back is not desired,
     * such as after a login or checkout process.
     *
     * @param route The destination route to navigate to.
     * @param popUpToRoute The route to pop up to before navigating. If null, the entire back stack is cleared.
     * @param inclusive If true, the [popUpToRoute] is also removed from the back stack.
     */
    fun navigateAndClearBackStack(route: String, popUpToRoute: String? = null, inclusive: Boolean = true)

    /**
     * Synchronizes the manager's state with the NavController's state.
     * This must be called by the UI host whenever the NavController's destination changes.
     *
     * @param route The new current route from the NavController.
     */
    fun onRouteChanged(route: String?)
}