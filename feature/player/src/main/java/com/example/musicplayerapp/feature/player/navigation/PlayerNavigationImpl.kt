package com.example.musicplayerapp.feature.player.navigation

import android.content.Context
import android.content.Intent
import com.example.musicplayerapp.core.navigation.PlayerNavigation
import com.example.musicplayerapp.feature.player.ui.musicplayer.MusicPlayerListActivity
import javax.inject.Inject

class PlayerNavigationImpl @Inject constructor() : PlayerNavigation {

    override fun getMusicPlayerListActivity(context: Context): Intent {
        return Intent(context, MusicPlayerListActivity::class.java)
    }
}