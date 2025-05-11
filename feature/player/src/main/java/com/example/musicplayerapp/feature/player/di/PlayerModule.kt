package com.example.musicplayerapp.feature.player.di

import android.content.Context
import com.example.musicplayerapp.feature.player.notification.MediaNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ServiceComponent::class)
object PlayerModule {

    @Provides
    fun provideMediaNotificationManager(
        @ApplicationContext context: Context
    ): MediaNotificationManager {
        return MediaNotificationManager(context)
    }
}
