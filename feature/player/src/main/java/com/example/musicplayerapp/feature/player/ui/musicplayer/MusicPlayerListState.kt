package com.example.musicplayerapp.feature.player.ui.musicplayer

import com.example.musicplayerapp.data.player.api.model.TrackList

internal data class MusicPlayerListState(
    val isLoading: Boolean = false,
    val musicList: List<TrackList> = emptyList(),
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isPlayingMusic: Boolean = false,
    val playedMusic: Int = 0,
)
