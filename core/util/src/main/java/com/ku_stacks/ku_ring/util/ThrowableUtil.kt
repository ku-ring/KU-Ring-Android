package com.ku_stacks.ku_ring.util

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.HttpException

fun Throwable.getHttpExceptionMessage(): String? = when(this) {
    is HttpException -> {
        val errorBody = response()?.errorBody()?.string()

        if (!errorBody.isNullOrBlank()) {
            Json.decodeFromString<ErrorMessage>(errorBody).resultMsg
        } else null
    }
    else -> null
}

@Serializable
private data class ErrorMessage(
    val resultMsg: String,
    val resultCode: Int,
    val isSuccess: Boolean,
)