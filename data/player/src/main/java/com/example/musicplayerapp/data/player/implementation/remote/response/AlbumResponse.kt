package com.example.musicplayerapp.data.player.implementation.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumResponse(
    @Json(name = "cover")
    val cover: String,

    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val title: String,
)