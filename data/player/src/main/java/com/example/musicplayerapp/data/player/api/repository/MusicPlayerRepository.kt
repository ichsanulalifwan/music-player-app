package com.example.musicplayerapp.data.player.api.repository

import com.example.musicplayerapp.core.data.ApiResponse
import com.example.musicplayerapp.data.player.api.model.Song

interface MusicPlayerRepository {

    suspend fun getMusicList(): ApiResponse<List<Song>>
}
