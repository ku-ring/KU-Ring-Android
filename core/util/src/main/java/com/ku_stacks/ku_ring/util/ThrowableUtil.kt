package com.ku_stacks.ku_ring.util

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

fun Throwable.getHttpExceptionMessage(): HttpExceptionMessage? = when (this) {
    is HttpException -> {
        val errorBody = response()?.errorBody()?.string()

        if (!errorBody.isNullOrBlank()) {
            runCatching {
                Json.decodeFromString<HttpExceptionMessage>(errorBody)
            }.fold(
                onSuccess = { it },
                onFailure = { null }
            )
        } else null
    }
    else -> null
}

@Serializable
data class HttpExceptionMessage(
    @SerialName("resultMsg")
    val message: String,
    @SerialName("resultCode")
    val code: Int,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
)