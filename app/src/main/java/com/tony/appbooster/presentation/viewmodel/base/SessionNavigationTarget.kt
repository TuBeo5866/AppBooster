package com.example.schedapp.presentation.viewmodel.base

/**
 * A model representing a navigation action triggered by a change in [SessionState].
 *
 * This class decouples the logic of *what* navigation should happen from the
 * session handling logic itself within the ViewModel.
 *
 * @property route The destination route for the navigation command.
 * @property isTerminal Defines if the corresponding [SessionState] is a "terminal" state.
 *                      A terminal state (like session expired or a critical error) is one
 *                      that should only be handled once to prevent navigation loops,
 *                      especially after configuration changes.
 */
internal data class SessionNavigationTarget(
    val route: String,
    val isTerminal: Boolean
)