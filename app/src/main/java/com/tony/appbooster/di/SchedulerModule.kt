package com.tony.appbooster.di

import com.tony.appbooster.data.scheduler.WorkManagerAnalysisWorkScheduler
import com.tony.appbooster.data.scheduler.WorkManagerOptimizationWorkScheduler
import com.tony.appbooster.domain.scheduler.AnalysisWorkScheduler
import com.tony.appbooster.domain.scheduler.OptimizationWorkScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt bindings for work scheduling abstractions.
 *
 * Business purpose:
 * - Keeps WorkManager details out of ViewModels and domain orchestration.
 * - Enables easy substitution with fake schedulers in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {

    /**
     * Binds WorkManager-backed analysis scheduling.
     */
    @Binds
    @Singleton
    abstract fun bindAnalysisWorkScheduler(
        impl: WorkManagerAnalysisWorkScheduler
    ): AnalysisWorkScheduler

    /**
     * Binds WorkManager-backed optimization scheduling.
     */
    @Binds
    @Singleton
    abstract fun bindOptimizationWorkScheduler(
        impl: WorkManagerOptimizationWorkScheduler
    ): OptimizationWorkScheduler
}
