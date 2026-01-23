package com.tony.appbooster.di

import com.alkemy.boxapp.presentation.navigation.interfaces.NavigationManager
import com.tony.appbooster.presentation.navigation.NavigationManagerImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    /**
     * Binds the [NavigationManagerImpl] to the [NavigationManager] interface
     * as a singleton.
     */
    @Binds
    @Singleton
    abstract fun bindNavigationManager(
        impl: NavigationManagerImpl
    ): NavigationManager
}