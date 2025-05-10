package com.example.musicplayerapp.di

import com.example.musicplayerapp.core.navigation.PlayerNavigation
import com.example.musicplayerapp.feature.player.navigation.PlayerNavigationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Module to host navigation
 */
@Module
@InstallIn(SingletonComponent::class)
internal interface NavigationModule {
    @Binds
    fun bindPlayerNavigation(
        demoNavigationImpl: PlayerNavigationImpl
    ): PlayerNavigation
}