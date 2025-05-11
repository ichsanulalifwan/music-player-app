package com.example.musicplayerapp.core.media.di

import com.example.musicplayerapp.core.media.player.MusicPlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
object MediaModule {

    @Provides
    fun provideMusicPlayerManager(): MusicPlayerManager {
        return MusicPlayerManager()
    }
}