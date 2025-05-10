package com.example.musicplayerapp.core.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "type")
    val type: String = "",
    @Json(name = "message")
    val message: String = "",
    @Json(name = "code")
    val code: String = "",
)
