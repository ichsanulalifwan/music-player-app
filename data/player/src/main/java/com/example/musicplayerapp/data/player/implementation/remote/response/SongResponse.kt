package com.example.musicplayerapp.data.player.implementation.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SongResponse(
    @Json(name = "album")
    val album: AlbumResponse,
    @Json(name = "artist")
    val artist: ArtistResponse,
    @Json(name = "duration")
    val duration: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "preview")
    val preview: String,
    @Json(name = "title")
    val title: String,
)
