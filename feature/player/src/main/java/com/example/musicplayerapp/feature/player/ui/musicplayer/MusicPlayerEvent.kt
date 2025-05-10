package com.example.musicplayerapp.feature.player.ui.musicplayer

sealed class MusicPlayerEvent {
    data object GetMusicList : MusicPlayerEvent()
    data class SearchMusic(
        val query: String = "",
    ) : MusicPlayerEvent()
}
