package com.example.musicplayerapp.core.network.ext

import com.example.musicplayerapp.core.network.response.ApiResult
import com.example.musicplayerapp.core.network.response.ErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

suspend fun <T : Any> result(
    remoteApi: suspend () -> Response<T>,
): ApiResult<T> = try {
    val response = remoteApi()
    handleResponse(response)
} catch (e: HttpException) {
    ApiResult.Error(codeResponse = e.code(), message = e.message.orEmpty(), e.message.orEmpty())
} catch (e: IOException) {
    ApiResult.Error(500, e.message.orEmpty(), e.message.orEmpty())
} catch (e: UnknownHostException) {
    ApiResult.Error(500, e.message.orEmpty(), e.message.orEmpty())
} catch (e: Exception) {
    ApiResult.Error(400, e.message.orEmpty(), e.message.orEmpty())
}

private fun <T : Any> handleResponse(response: Response<T>): ApiResult<T> {
    val body = response.body()

    return if (response.isSuccessful && body != null) {
        ApiResult.Success(data = body)
    } else {
        val error = response.errorBody()?.source()?.let {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()
                .adapter(ErrorResponse::class.java)
                .fromJson(it)
        }

        ApiResult.Error(
            codeResponse = response.code(),
            message = error?.message.orEmpty(),
            type = error?.type.orEmpty(),
        )
    }
}
