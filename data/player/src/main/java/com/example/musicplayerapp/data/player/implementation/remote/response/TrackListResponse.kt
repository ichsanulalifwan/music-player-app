package com.example.musicplayerapp.data.player.implementation.remote.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrackListResponse(
    @Json(name = "tracks")
    val tracksResponse: TracksResponse,
)