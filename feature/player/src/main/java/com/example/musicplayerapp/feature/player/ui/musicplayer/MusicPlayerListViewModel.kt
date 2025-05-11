package com.example.musicplayerapp.feature.player.ui.musicplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayerapp.core.data.ApiResponse
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.data.player.api.repository.MusicPlayerRepository
import com.example.musicplayerapp.feature.player.service.MusicPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MusicPlayerListViewModel @Inject constructor(
    private val musicPlayerRepository: MusicPlayerRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlayerListState())
    val state = _state.asStateFlow()

    fun onEvent(event: MusicPlayerEvent) {
        when (event) {
            is MusicPlayerEvent.GetMusicList -> {
                loadingData()
                getMusicList()
            }

            is MusicPlayerEvent.UpdatePlayerState -> {
                updatePlayerStateFromBinder(binder = event.binder)
            }
        }
    }

    private fun loadingData() {
        _state.update {
            it.copy(isLoading = true)
        }
    }

    private fun getMusicList() = viewModelScope.launch {
        val response = musicPlayerRepository.getMusicList()
        handleMusicListResponse(response = response)
    }

    private fun handleMusicListResponse(
        response: ApiResponse<List<Song>>,
    ) {
        when (response) {
            is ApiResponse.Loading -> {
                _state.update {
                    it.copy(isLoading = true)
                }
            }

            is ApiResponse.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        musicList = response.data,
                    )
                }
            }

            is ApiResponse.Error -> {
                _state.update {
                    it.copy(
                        isError = true,
                        isLoading = false,
                        errorMessage = response.message,
                    )
                }
            }
        }
    }

    private fun updatePlayerStateFromBinder(binder: MusicPlayerService.MusicBinder) {
        viewModelScope.launch {
            binder.getCurrentTrack().collect { song ->
                _state.update { it.copy(currentTrack = song) }
            }
        }

        viewModelScope.launch {
            binder.isPlaying().collect { playing ->
                _state.update { it.copy(isPlaying = playing) }
            }
        }

        viewModelScope.launch {
            binder.maxDuration().collect { max ->
                _state.update { it.copy(maxDuration = max) }
            }
        }

        viewModelScope.launch {
            binder.currentDuration().collect { current ->
                _state.update { it.copy(currentDuration = current) }
            }
        }
    }
}
