package com.example.musicplayerapp.core.network.response

sealed class ApiResult<out T> {
    data class Success<out T>(
        val data: T,
    ) : ApiResult<T>()

    data class Error(
        val codeResponse: Int,
        val message: String,
        val type: String,
    ) : ApiResult<Nothing>()
}
