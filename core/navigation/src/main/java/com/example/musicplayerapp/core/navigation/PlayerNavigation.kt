package com.example.musicplayerapp.core.navigation

import android.content.Context
import android.content.Intent

interface PlayerNavigation {
    fun getMusicPlayerListActivity(context: Context): Intent
}