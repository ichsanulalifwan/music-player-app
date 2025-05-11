package com.example.musicplayerapp.feature.player.ui.musicplayer

import com.example.musicplayerapp.data.player.api.model.Song

internal data class MusicPlayerListState(
    val isLoading: Boolean = false,
    val musicList: List<Song> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isPlaying: Boolean = false,
    val maxDuration: Float = 0f,
    val currentDuration: Float = 0f,
    val currentTrack: Song = Song(),
)
