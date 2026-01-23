package com.tony.appbooster.presentation.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.alkemy.boxapp.presentation.navigation.interfaces.NavigationManager
import com.tony.appbooster.domain.model.common.Resource
import com.tony.appbooster.domain.model.settings.AppOptimizationType
import com.tony.appbooster.domain.repository.AdbRepository
import com.tony.appbooster.domain.usecase.ConnectAdbUseCase
import com.tony.appbooster.domain.usecase.GetAdbConnectionConfigUseCase
import com.tony.appbooster.domain.usecase.ObserveAppOptimizationTypeUseCase
import com.tony.appbooster.domain.usecase.OptimizeAppUseCase
import com.tony.appbooster.domain.usecase.UpdateAdbHostUseCase
import com.tony.appbooster.domain.usecase.UpdateAdbPairingCodeUseCase
import com.tony.appbooster.domain.usecase.UpdateAdbPortUseCase
import com.tony.appbooster.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for orchestrating the ADB connection and optimization
 * flows and exposing them as a unified [MainUiModel] state for the setup screen.
 *
 * The business purpose is to coordinate wireless ADB discovery, connection,
 * and ART optimization while surfacing progress, logs, and errors to the UI.
 *
 * @param connectAdbUseCase Use case that discovers the wireless debug port and connects to ADB.
 * @param optimizeAppUseCase Use case that triggers ART optimization on the connected device.
 * @param repository Repository exposing ADB connection, logs, and optimization progress.
 * @param navigationManager Manager used to dispatch navigation commands from the ViewModel.
 * @return A ViewModel instance exposing a single `StateFlow<UIState<MainUiModel>>`.
 * @throws IllegalStateException If required dependencies are not provided by DI.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectAdbUseCase: ConnectAdbUseCase,
    private val optimizeAppUseCase: OptimizeAppUseCase,
    private val getOptimizeAppUseCase: ObserveAppOptimizationTypeUseCase,
    private val saveHostUseCase: UpdateAdbHostUseCase,
    private val savePortUseCase: UpdateAdbPortUseCase,
    private val savePairingCodeUseCase: UpdateAdbPairingCodeUseCase,
    private val getAdbConnectionConfigUseCase: GetAdbConnectionConfigUseCase,
    private val repository: AdbRepository,
    navigationManager: NavigationManager
) : BaseViewModel<MainUiModel>(navigationManager) {

    override val LOG_TAG: String = "MainViewModel"


    init {
        observeRepository()
        observeOptimizationMode()
        loadAdbInfoData()
    }

    /**
     * Observes the persisted optimization mode setting so the Dashboard UI
     * can reflect changes immediately after the user updates Settings.
     */
    private fun observeOptimizationMode() {
        viewModelScope.launch(exceptionHandler) {
            getOptimizeAppUseCase()
                .collect { resource ->
                    val current = uiState.value.data ?: MainUiModel()
                    val newMode = when (resource) {
                        is Resource.Success -> resource.data
                        is Resource.Error -> current.optimizationMode
                    }
                    updateUiData(current.copy(optimizationMode = newMode))
                }
        }
    }

    fun loadAdbInfoData(){
        launchUiStateUpdate(
            dataFetchBlock = { getAdbConnectionConfigUseCase().first() },
            processSuccess = {
                val currentState = uiState.value.data ?: MainUiModel()
                currentState.copy(
                    adbHost = it.host,
                    adbPort = it.port,
                    adbPairingCode = it.pairingCode
                )
            }
        )
    }

    /**
     * Starts the wireless ADB setup flow by discovering the port and connecting
     * to the local ADB instance. The result is pushed through [launchUiStateUpdate],
     * updating the UI status and surfacing any domain errors.
     *
     * @return Unit when the asynchronous operation has been launched.
     * @throws IllegalStateException If the use case throws unexpectedly.
     */
    fun startConnectionSequence() {
        launchUiStateUpdate(
            dataFetchBlock = { connectAdbUseCase() },
            processSuccess = {
                uiState.value.data ?: MainUiModel()
            }
        )
    }

    /**
     * Requests cancellation of an active optimization run.
     *
     * The business purpose is to allow the user to stop long-running work
     * without leaving the UI in a loading state.
     */
    fun stopOptimization() {
        launchUiStateUpdate(
            dataFetchBlock = { repository.cancelOptimization() },
            skipLoading = true,
            processSuccess = {
                // Keep current UI data; progress/logs are emitted from repository flows.
                uiState.value.data ?: MainUiModel()
            }
        )
    }

    /**
     * Starts ART optimization using a predefined compile mode on the active ADB session.
     * The progress is streamed from the repository, while the final result and errors
     * are handled via [launchUiStateUpdate].
     *
     * @param mode The compile mode to be used for optimization.
     *
     * @return Unit when the asynchronous operation has been launched.
     * @throws IllegalStateException If the use case throws unexpectedly.
     * @throws IllegalStateException If the coroutine scope is not available.
     */
    fun startOptimization(mode: AppOptimizationType) {

        launchUiStateUpdate(
            dataFetchBlock = { optimizeAppUseCase(mode) },
            skipLoading = true,
            processSuccess = {
                uiState.value.data ?: MainUiModel()
            }
        )
    }

    /**
     * Starts ART optimization using a predefined compile mode on the active ADB session.
     * The progress is streamed from the repository, while the final result and errors
     */
    fun runAppOptimization() {
        // Optimization mode is observed continuously; use the latest value.
        val optimizationMode = uiState.value.data?.optimizationMode
        if (optimizationMode != null) {
            startOptimization(optimizationMode)
        }
    }

    /**
     * Observes ADB connection, command output and optimization progress from the
     * repository and maps them into a single [MainUiModel] pushed to the UI.
     *
     * This keeps the UI reactive to underlying ADB state without duplicating
     * logic in the composables.
     *
     * @return Unit when the observation coroutine has been launched.
     * @throws IllegalStateException If the coroutine scope is not available.
     */
    private fun observeRepository() {
        viewModelScope.launch(exceptionHandler) {
            combine(
                repository.connectionState,
                repository.commandOutput,
                repository.optimizationProgress
            ) { connectionState, logs, progress ->
                MainUiModel(
                    connectionState = connectionState,
                    logs = logs,
                    optimizationProgress = progress
                )
            }.collect { model ->
                // Reuse BaseViewModel helper to update only data while preserving status.
                updateUiData(model)
            }
        }
    }
}
