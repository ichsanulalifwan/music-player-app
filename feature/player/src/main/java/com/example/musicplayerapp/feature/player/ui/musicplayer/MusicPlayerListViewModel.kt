package com.example.musicplayerapp.feature.player.ui.musicplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayerapp.core.data.ApiResponse
import com.example.musicplayerapp.data.player.api.model.TrackList
import com.example.musicplayerapp.data.player.api.repository.MusicPlayerRepository
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

            is MusicPlayerEvent.SearchMusic -> {
                loadingData()
                clearMusics()
                searchMusicList(artistName = event.query)
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
        response: ApiResponse<List<TrackList>>,
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

    private fun searchMusicList(artistName: String) = viewModelScope.launch {

    }

    private fun clearMusics() {
        _state.update {
            it.copy(
                musicList = emptyList(),
            )
        }
    }
}
