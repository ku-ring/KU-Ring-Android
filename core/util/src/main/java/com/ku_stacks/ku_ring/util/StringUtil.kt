package com.ku_stacks.ku_ring.util

import kotlinx.datetime.LocalDateTime

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

infix fun String.or(that: String): String = if (BuildConfig.DEBUG) this else that

fun String.isOnlyAlphabets() = matches("[a-zA-Z]*$".toRegex())

fun String.toLocalDateTimeOrNull(): LocalDateTime? {
    return try {
        if (this.isEmpty()) null else LocalDateTime.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.percentEncode(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}