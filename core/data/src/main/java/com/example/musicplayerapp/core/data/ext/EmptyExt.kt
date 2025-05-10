package com.example.musicplayerapp.core.data.ext

fun Int?.orEmpty(): Int {
    return this ?: 0
}
