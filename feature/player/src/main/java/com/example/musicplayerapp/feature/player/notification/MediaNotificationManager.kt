package com.example.musicplayerapp.feature.player.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.example.musicplayerapp.core.media.utils.CHANNEL_ID
import com.example.musicplayerapp.core.media.utils.NEXT
import com.example.musicplayerapp.core.media.utils.PLAY_PAUSE
import com.example.musicplayerapp.core.media.utils.PREV
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.feature.player.service.MusicPlayerService
import javax.inject.Inject
import com.example.musicplayerapp.core.ui.R as RCore

class MediaNotificationManager @Inject constructor(
    private val context: Context
) {

    fun createNotification(track: Song, isPlaying: Boolean): Notification {
        val session = MediaSessionCompat(context, "music")

        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setMediaSession(session.sessionToken)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(track.title)
            .setContentText(track.artist.name)
            .setSmallIcon(RCore.drawable.ic_launcher_background)
            .addAction(RCore.drawable.ic_prev, "Prev", createPendingIntent(PREV))
            .addAction(
                if (isPlaying) RCore.drawable.ic_pause else RCore.drawable.ic_play,
                "PlayPause",
                createPendingIntent(PLAY_PAUSE)
            )
            .addAction(RCore.drawable.ic_next, "Next", createPendingIntent(NEXT))
            .setStyle(style)
            .build()
    }

    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(context, MusicPlayerService::class.java).apply { this.action = action }
        return PendingIntent.getService(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}
