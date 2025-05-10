package com.example.musicplayerapp.core.data

sealed interface ApiResponse<out T> {
    data object Loading : ApiResponse<Nothing>

    open class Error(open val message: String) : ApiResponse<Nothing>

    data class Success<T>(val data: T) : ApiResponse<T>
}
