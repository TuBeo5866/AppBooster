package com.tony.appbooster.di

import com.tony.appbooster.data.client.AdbShellClientImpl
import com.tony.appbooster.data.repository.AdbRepositoryImpl
import com.tony.appbooster.data.repository.AdbShellDataSourceImpl
import com.tony.appbooster.domain.client.AdbShellClient
import com.tony.appbooster.domain.client.AdbShellDataSource
import com.tony.appbooster.domain.client.ShizukuShellClient
import com.tony.appbooster.domain.repository.AdbRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier for the IO dispatcher used for blocking ADB operations.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AdbIoDispatcher

/**
 * Hilt bindings for ADB data layer abstractions.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AdbBindingsModule {

    /**
     * Binds the Shizuku-based implementation as the concrete ADB shell client.
     *
     * @param impl Implementation that uses Shizuku for privileged shell access.
     * @return Bound [AdbShellClient] abstraction.
     */
    @Binds
    @Singleton
    abstract fun bindAdbShellClient(
        impl: AdbShellClientImpl
    ): AdbShellClient

    /**
     * Binds the concrete data-layer implementation of [AdbRepository].
     *
     * @param impl Concrete repository that coordinates shell execution and
     * connection state using the configured ADB client.
     * @return Bound domain-level [AdbRepository] abstraction.
     */
    @Binds
    @Singleton
    abstract fun bindAdbRepository(
        impl: AdbRepositoryImpl
    ): AdbRepository

    @Binds
    @Singleton
    abstract fun bindAdbShellDataSource(
        impl: AdbShellDataSourceImpl
    ): AdbShellDataSource
}

/**
 * Hilt providers for ADB configuration.
 */
@Module
@InstallIn(SingletonComponent::class)
object AdbConfigModule {

    /**
     * Provides the dispatcher used for blocking ADB/shell calls.
     *
     * @return [CoroutineDispatcher] configured for IO-bound work.
     */
    @Provides
    @Singleton
    @AdbIoDispatcher
    fun provideAdbIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides a fully configured [AdbShellClientImpl] instance.
     *
     * @param shizukuClient The Shizuku shell client for privileged operations.
     * @return Configured [AdbShellClientImpl].
     */
    @Provides
    @Singleton
    fun provideAdbShellClientImpl(
        shizukuClient: ShizukuShellClient
    ): AdbShellClientImpl {
        return AdbShellClientImpl(shizukuClient)
    }
}
