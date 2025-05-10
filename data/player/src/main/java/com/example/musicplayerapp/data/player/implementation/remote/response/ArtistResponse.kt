package com.example.musicplayerapp.data.player.implementation.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistResponse(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,
)