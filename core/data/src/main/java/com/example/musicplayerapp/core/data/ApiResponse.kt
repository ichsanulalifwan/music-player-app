package com.example.musicplayerapp.core.data

sealed interface ApiResponse<out T> {
    data object Loading : ApiResponse<Nothing>

    data class Error(val message: String) : ApiResponse<Nothing>

    data class Success<T>(val data: T) : ApiResponse<T>
}
