package com.example.musicplayerapp.data.player.implementation.repository

import com.example.musicplayerapp.core.data.ApiResponse
import com.example.musicplayerapp.core.network.ext.result
import com.example.musicplayerapp.core.network.response.ApiResult
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.data.player.api.repository.MusicPlayerRepository
import com.example.musicplayerapp.data.player.implementation.mapper.toSong
import com.example.musicplayerapp.data.player.implementation.remote.api.MusicPlayerApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class MusicPlayerRepositoryImpl(
    private val musicPlayerApi: MusicPlayerApi,
    private val ioDispatcher: CoroutineDispatcher,
) : MusicPlayerRepository {

    override suspend fun getMusicList(): ApiResponse<List<Song>> {
        return withContext(ioDispatcher) {
            val apiResult = result {
                musicPlayerApi.getMusicList()
            }

            when (apiResult) {
                is ApiResult.Success -> {
                    val musicResult = apiResult.data.tracksResponse.data.map { it.toSong() }
                    ApiResponse.Success(data = musicResult)
                }

                is ApiResult.Error -> {
                    ApiResponse.Error(apiResult.message)
                }
            }
        }
    }
}
