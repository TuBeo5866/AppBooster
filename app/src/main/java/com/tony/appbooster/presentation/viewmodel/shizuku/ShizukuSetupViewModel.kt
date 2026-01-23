package com.tony.appbooster.presentation.viewmodel.shizuku

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tony.appbooster.domain.model.shizuku.ShizukuState
import com.tony.appbooster.domain.usecase.shizuku.ObserveShizukuStateUseCase
import com.tony.appbooster.domain.usecase.shizuku.OpenShizukuAppUseCase
import com.tony.appbooster.domain.usecase.shizuku.OpenShizukuInstallPageUseCase
import com.tony.appbooster.domain.usecase.shizuku.RefreshShizukuStateUseCase
import com.tony.appbooster.domain.usecase.shizuku.RequestShizukuPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Shizuku setup flow.
 *
 * Manages the state of Shizuku authorization and guides the user through
 * the setup process step by step.
 */
@HiltViewModel
class ShizukuSetupViewModel @Inject constructor(
    private val observeShizukuState: ObserveShizukuStateUseCase,
    private val refreshShizukuState: RefreshShizukuStateUseCase,
    private val requestPermission: RequestShizukuPermissionUseCase,
    private val openInstallPage: OpenShizukuInstallPageUseCase,
    private val openShizukuApp: OpenShizukuAppUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShizukuSetupUiModel())
    val uiState: StateFlow<ShizukuSetupUiModel> = _uiState.asStateFlow()

    init {
        observeShizukuStateChanges()
        refreshState()
    }

    private fun observeShizukuStateChanges() {
        observeShizukuState()
            .onEach { shizukuState ->
                Log.d(TAG, "Shizuku state changed: $shizukuState")
                _uiState.update { current ->
                    current.copy(
                        shizukuState = shizukuState,
                        setupStep = mapStateToStep(shizukuState),
                        isCheckingState = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun mapStateToStep(state: ShizukuState): ShizukuSetupStep {
        return when (state) {
            ShizukuState.NotInstalled -> ShizukuSetupStep.INSTALL_SHIZUKU
            ShizukuState.NotRunning -> ShizukuSetupStep.START_SERVICE
            ShizukuState.PermissionRequired -> ShizukuSetupStep.GRANT_PERMISSION
            ShizukuState.Ready -> ShizukuSetupStep.READY
            is ShizukuState.Error -> ShizukuSetupStep.CHECK_STATUS
        }
    }

    /**
     * Refreshes the current Shizuku state.
     * Call when the screen becomes visible or user taps refresh.
     */
    fun refreshState() {
        Log.d(TAG, "refreshState() called")
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingState = true) }
            refreshShizukuState()
            // Always reset checking state after refresh completes
            _uiState.update { it.copy(isCheckingState = false) }
        }
    }

    /**
     * Opens the Shizuku download/install page.
     */
    fun onInstallShizukuClicked() {
        openInstallPage()
    }

    /**
     * Opens the Shizuku app to help user start the service.
     */
    fun onOpenShizukuClicked() {
        openShizukuApp()
    }

    /**
     * Requests Shizuku permission from the user.
     */
    fun onRequestPermissionClicked() {
        Log.d(TAG, "onRequestPermissionClicked() called")
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingState = true) }
            Log.d(TAG, "Calling requestPermission use case...")
            requestPermission()
            Log.d(TAG, "requestPermission use case completed")
            // Reset checking state after request completes
            _uiState.update { it.copy(isCheckingState = false) }
        }
    }

    /**
     * Called when user returns from external action (install, open app).
     */
    fun onResumed() {
        refreshState()
    }

    companion object {
        private const val TAG = "ShizukuSetupViewModel"
    }
}
