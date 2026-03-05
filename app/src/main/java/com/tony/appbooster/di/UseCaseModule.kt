package com.tony.appbooster.di


import com.tony.appbooster.domain.repository.AdbRepository
import com.tony.appbooster.domain.repository.AppInfoRepository
import com.tony.appbooster.domain.repository.SettingsRepository
import com.tony.appbooster.domain.usecase.adb.ConnectAdbUseCase
import com.tony.appbooster.domain.usecase.appinfo.GetAppInfoUseCase
import com.tony.appbooster.domain.usecase.optimization.OptimizeAppUseCase
import com.tony.appbooster.domain.usecase.settings.ObserveAppOptimizationTypeUseCase
import com.tony.appbooster.domain.usecase.settings.SetAppOptimizationTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that exposes all domain use cases as injectable dependencies so
 * that presentation-layer components can orchestrate shell connectivity,
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
}
