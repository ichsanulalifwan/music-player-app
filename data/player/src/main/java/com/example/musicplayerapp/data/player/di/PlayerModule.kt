package com.example.musicplayerapp.data.player.di

import com.example.musicplayerapp.core.coroutines.qualifier.IoDispatcher
import com.example.musicplayerapp.data.player.api.repository.MusicPlayerRepository
import com.example.musicplayerapp.data.player.implementation.remote.api.MusicPlayerApi
import com.example.musicplayerapp.data.player.implementation.repository.MusicPlayerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayerModule {
    @Provides
    @Singleton
    fun provideMusicPlayerApi(retrofit: Retrofit): MusicPlayerApi {
        return retrofit.create(MusicPlayerApi::class.java)
    }

    @Provides
    fun provideMusicPlayerRepository(
        musicPlayerApi: MusicPlayerApi,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): MusicPlayerRepository {
        return MusicPlayerRepositoryImpl(
            musicPlayerApi,
            ioDispatcher,
        )
    }
}
