package com.tony.appbooster.di

import android.content.Context
import com.tony.appbooster.data.client.ShizukuShellClientImpl
import com.tony.appbooster.domain.client.ShizukuShellClient
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 * Hilt module providing Shizuku-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ShizukuBindingsModule {

    /**
     * Binds the Shizuku shell client implementation.
     */
    @Binds
    @Singleton
    abstract fun bindShizukuShellClient(
        impl: ShizukuShellClientImpl
    ): ShizukuShellClient
}

@Module
@InstallIn(SingletonComponent::class)
object ShizukuProviderModule {

    /**
     * Provides the ShizukuShellClientImpl instance.
     */
    @Provides
    @Singleton
    fun provideShizukuShellClient(
        @ApplicationContext context: Context,
        @AdbIoDispatcher ioDispatcher: CoroutineDispatcher
    ): ShizukuShellClientImpl {
        return ShizukuShellClientImpl(context, ioDispatcher)
    }
}
