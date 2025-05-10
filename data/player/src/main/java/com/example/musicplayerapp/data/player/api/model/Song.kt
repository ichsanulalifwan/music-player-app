package com.example.musicplayerapp.data.player.api.model

data class Song(
    val album: Album = Album(),
    val artist: Artist = Artist(),
    val duration: Int = 0,
    val id: Int = 0,
    val preview: String = "",
    val title: String = "",
)
