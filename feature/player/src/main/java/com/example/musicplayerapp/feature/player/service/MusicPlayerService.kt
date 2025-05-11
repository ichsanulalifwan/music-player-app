package com.example.musicplayerapp.feature.player.service

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.example.musicplayerapp.core.media.player.MusicPlayerManager
import com.example.musicplayerapp.core.media.utils.NEXT
import com.example.musicplayerapp.core.media.utils.PLAY_PAUSE
import com.example.musicplayerapp.core.media.utils.PREV
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.feature.player.notification.MediaNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : Service() {

    @Inject
    lateinit var musicPlayerManager: MusicPlayerManager

    @Inject
    lateinit var notificationManager: MediaNotificationManager

    val binder = MusicBinder()

    override fun onCreate() {
        super.onCreate()
    }

    inner class MusicBinder : Binder() {
        fun getService() = this@MusicPlayerService
        fun getMusicPlayerManager() = musicPlayerManager
        fun setMusicList(list: List<Song>) = musicPlayerManager.setMusicList(list)
        fun isPlaying() = musicPlayerManager.isPlaying
        fun maxDuration() = musicPlayerManager.maxDuration
        fun currentDuration() = musicPlayerManager.currentDuration
        fun getCurrentTrack() = musicPlayerManager.currentTrack
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            PREV -> musicPlayerManager.prev()
            NEXT -> musicPlayerManager.next()
            PLAY_PAUSE -> musicPlayerManager.playPause()
            else -> musicPlayerManager.playDefault()
        }
        updateNotification()
        return START_STICKY
    }

    private fun updateNotification() {
        val track = musicPlayerManager.currentTrack.value
        val isPlaying = musicPlayerManager.isPlaying.value
        val notification = notificationManager.createNotification(track, isPlaying)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED) {
            startForeground(1, notification)
        } else {
            startForeground(1, notification)
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        musicPlayerManager.release()
        super.onDestroy()
    }
}
