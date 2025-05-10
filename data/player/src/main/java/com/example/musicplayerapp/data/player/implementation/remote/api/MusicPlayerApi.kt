package com.example.musicplayerapp.data.player.implementation.remote.api

import com.example.musicplayerapp.data.player.implementation.remote.response.TrackListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MusicPlayerApi {

    @GET("2.0/playlist/{playlist_id}")
    suspend fun getMusicList(
        @Path("playlist_id") playlistId: String = "908622995",
    ): Response<List<TrackListResponse>>
}