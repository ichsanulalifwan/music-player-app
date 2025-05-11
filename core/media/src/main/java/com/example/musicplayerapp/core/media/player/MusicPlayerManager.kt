package com.example.musicplayerapp.core.media.player

import android.media.MediaPlayer
import com.example.musicplayerapp.data.player.api.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class MusicPlayerManager @Inject constructor() {
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _currentTrack = MutableStateFlow(Song())
    val currentTrack: StateFlow<Song> get() = _currentTrack

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private val _maxDuration = MutableStateFlow(0f)
    val maxDuration: StateFlow<Float> get() = _maxDuration

    private val _currentDuration = MutableStateFlow(0f)
    val currentDuration: StateFlow<Float> get() = _currentDuration

    private var musicList: List<Song> = emptyList()
    private var updateJob: Job? = null

    fun setMusicList(list: List<Song>) {
        musicList = list
    }

    fun playDefault() {
        musicList.firstOrNull()?.let { play(it) }
    }

    fun play(song: Song) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.preview)
            setOnPreparedListener {
                start()
                _isPlaying.value = true
                _maxDuration.value = duration.toFloat()
                updateDurations()
            }
            prepareAsync()
        }

        _currentTrack.value = song
    }

    fun playPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            } else {
                it.start()
                _isPlaying.value = true
            }
        }
    }

    fun prev() {
        val index = musicList.indexOf(currentTrack.value)
        val prev = if (index > 0) musicList[index - 1] else musicList.lastOrNull()
        prev?.let { play(it) }
    }

    fun next() {
        val index = musicList.indexOf(currentTrack.value)
        val next = if (index < musicList.lastIndex) musicList[index + 1] else musicList.firstOrNull()
        next?.let { play(it) }
    }

    private fun updateDurations() {
        updateJob?.cancel()
        updateJob = scope.launch {
            while (isActive && mediaPlayer?.isPlaying == true) {
                _currentDuration.value = mediaPlayer?.currentPosition?.toFloat() ?: 0f
                delay(1000)
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        updateJob?.cancel()
    }
}
