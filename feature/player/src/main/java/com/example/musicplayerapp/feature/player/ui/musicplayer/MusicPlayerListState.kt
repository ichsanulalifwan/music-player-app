package com.example.musicplayerapp.feature.player.ui.musicplayer

import com.example.musicplayerapp.data.player.api.model.Song

internal data class MusicPlayerListState(
    val isLoading: Boolean = false,
    val musicList: List<Song> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isPlayingMusic: Boolean = false,
    val playedMusic: Int = 0,
)
