package com.ku_stacks.ku_ring.util

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

fun Throwable.getHttpExceptionMessage(): String? = when (this) {
    is HttpException -> {
        val errorBody = response()?.errorBody()?.string()

        if (!errorBody.isNullOrBlank()) {
            runCatching {
                Json.decodeFromString<ErrorMessage>(errorBody).resultMessage
            }.fold(
                onSuccess = { it },
                onFailure = { null }
            )
        } else null
    }
    else -> null
}

@Serializable
private data class ErrorMessage(
    @SerialName("resultMsg")
    val resultMessage: String,
    val resultCode: Int,
    val isSuccess: Boolean,
)