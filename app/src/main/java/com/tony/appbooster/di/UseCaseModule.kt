package com.tony.appbooster.di


import com.tony.appbooster.domain.repository.AdbRepository
import com.tony.appbooster.domain.repository.AppInfoRepository
import com.tony.appbooster.domain.repository.SettingsRepository
import com.tony.appbooster.domain.usecase.ConnectAdbUseCase
import com.tony.appbooster.domain.usecase.GetAdbConnectionConfigUseCase
import com.tony.appbooster.domain.usecase.GetAppInfoUseCase
import com.tony.appbooster.domain.usecase.ObserveAdbHostUseCase
import com.tony.appbooster.domain.usecase.ObserveAdbPairingCodeUseCase
import com.tony.appbooster.domain.usecase.ObserveAdbPortUseCase
import com.tony.appbooster.domain.usecase.ObserveAppOptimizationTypeUseCase
import com.tony.appbooster.domain.usecase.OptimizeAppUseCase
import com.tony.appbooster.domain.usecase.SetAppOptimizationTypeUseCase
import com.tony.appbooster.domain.usecase.UpdateAdbHostUseCase
import com.tony.appbooster.domain.usecase.UpdateAdbPortUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that exposes all domain use cases as injectable dependencies so
 * that presentation-layer components can orchestrate ADB connectivity,
 * application optimization, and settings management without depending
 * directly on repository implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    /**
     * Provides a use case that ensures Shizuku-based shell connection is ready.
     *
     * @param adbRepository Repository that exposes ADB operations.
     * @return [ConnectAdbUseCase] used to verify shell access is available.
     */
    @Provides
    @Singleton
    fun provideConnectAdbUseCase(
        adbRepository: AdbRepository
    ): ConnectAdbUseCase = ConnectAdbUseCase(adbRepository)

    /**
     * Provides a use case that retrieves application metadata required by
     * the Settings and informational screens through the [AppInfoRepository].
     *
     * @param appInfoRepository Repository that exposes app info data.
     * @return [GetAppInfoUseCase] used to load current app metadata.
     */
    @Provides
    @Singleton
    fun provideGetAppInfoUseCase(
        appInfoRepository: AppInfoRepository
    ): GetAppInfoUseCase = GetAppInfoUseCase(appInfoRepository)

    /**
     * Provides a use case that exposes a combined ADB host and port stream as
     * a single connection configuration used across debugging flows.
     *
     * @param settingsRepository Repository exposing ADB settings streams.
     * @return [GetAdbConnectionConfigUseCase] used to observe effective ADB configuration.
     */
    @Provides
    @Singleton
    fun provideGetAdbConnectionConfigUseCase(
        settingsRepository: SettingsRepository
    ): GetAdbConnectionConfigUseCase = GetAdbConnectionConfigUseCase(settingsRepository)

    /**
     * Provides a use case that observes ADB host changes from persisted
     * settings so the UI can reactively update connection-related state.
     *
     * @param settingsRepository Repository exposing the ADB host stream.
     * @return [ObserveAdbHostUseCase] used to observe ADB host updates.
     */
    @Provides
    @Singleton
    fun provideObserveAdbHostUseCase(
        settingsRepository: SettingsRepository
    ): ObserveAdbHostUseCase = ObserveAdbHostUseCase(settingsRepository)

    /**
     * Provides a use case that observes ADB port changes from persisted
     * settings, allowing real-time updates of connection parameters.
     *
     * @param settingsRepository Repository exposing the ADB port stream.
     * @return [ObserveAdbPortUseCase] used to observe ADB port updates.
     */
    @Provides
    @Singleton
    fun provideObserveAdbPortUseCase(
        settingsRepository: SettingsRepository
    ): ObserveAdbPortUseCase = ObserveAdbPortUseCase(settingsRepository)

    /**
     * Provides a use case that observes the active optimization mode so the
     * presentation layer can synchronize UI options with stored settings.
     *
     * @param settingsRepository Repository that manages optimization settings.
     * @return [ObserveAppOptimizationTypeUseCase] used to observe optimization type updates.
     */
    @Provides
    @Singleton
    fun provideObserveAppOptimizationTypeUseCase(
        settingsRepository: SettingsRepository
    ): ObserveAppOptimizationTypeUseCase =
        ObserveAppOptimizationTypeUseCase(settingsRepository)

    /**
     * Provides a use case that triggers ART optimization on installed
     * applications using the currently selected optimization strategy.
     *
     * @param adbRepository Repository that executes optimization commands via ADB.
     * @return [OptimizeAppUseCase] used to run optimization over the ADB session.
     */
    @Provides
    @Singleton
    fun provideOptimizeAppUseCase(
        adbRepository: AdbRepository
    ): OptimizeAppUseCase = OptimizeAppUseCase(adbRepository)

    /**
     * Provides a use case that persists the selected optimization behavior
     * so that future optimization commands follow the user preference.
     *
     * @param settingsRepository Repository that stores optimization configuration.
     * @return [SetAppOptimizationTypeUseCase] used to update optimization type.
     */
    @Provides
    @Singleton
    fun provideSetAppOptimizationTypeUseCase(
        settingsRepository: SettingsRepository
    ): SetAppOptimizationTypeUseCase =
        SetAppOptimizationTypeUseCase(settingsRepository)

    /**
     * Provides a use case that updates and persists the ADB host used by
     * wireless debugging and optimization workflows.
     *
     * @param settingsRepository Repository that persists the ADB host value.
     * @return [UpdateAdbHostUseCase] used to change the configured ADB host.
     */
    @Provides
    @Singleton
    fun provideUpdateAdbHostUseCase(
        settingsRepository: SettingsRepository
    ): UpdateAdbHostUseCase = UpdateAdbHostUseCase(settingsRepository)

    /**
     * Provides a use case that updates and persists the ADB port used by
     * wireless debugging and optimization workflows.
     *
     * @param settingsRepository Repository that persists the ADB port value.
     * @return [UpdateAdbPortUseCase] used to change the configured ADB port.
     */
    @Provides
    @Singleton
    fun provideUpdateAdbPortUseCase(
        settingsRepository: SettingsRepository
    ): UpdateAdbPortUseCase = UpdateAdbPortUseCase(settingsRepository)

    /**
     * Provides a use case that updates and persists the ADB port used by
     * wireless debugging and optimization workflows.
     *
     * @param settingsRepository Repository that persists the ADB port value.
     * @return [UpdateAdbPortUseCase] used to change the configured ADB port.
     */
    @Provides
    @Singleton
    fun provideUpdateAdbPairingCodeUseCase(
        settingsRepository: SettingsRepository
    ): ObserveAdbPairingCodeUseCase = ObserveAdbPairingCodeUseCase(settingsRepository)
}
