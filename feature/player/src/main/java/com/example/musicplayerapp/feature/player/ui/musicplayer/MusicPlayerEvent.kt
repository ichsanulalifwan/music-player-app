package com.example.musicplayerapp.feature.player.ui.musicplayer

import com.example.musicplayerapp.feature.player.service.MusicPlayerService

sealed class MusicPlayerEvent {
    data object GetMusicList : MusicPlayerEvent()
    data class UpdatePlayerState(val binder: MusicPlayerService.MusicBinder) : MusicPlayerEvent()
}
