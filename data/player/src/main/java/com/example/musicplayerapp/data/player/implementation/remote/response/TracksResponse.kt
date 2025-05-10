package com.example.musicplayerapp.data.player.implementation.remote.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TracksResponse(
    @Json(name = "data")
    val `data`: List<SongResponse>
)
