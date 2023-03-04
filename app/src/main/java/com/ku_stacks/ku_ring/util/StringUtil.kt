package com.ku_stacks.ku_ring.util

import com.ku_stacks.ku_ring.BuildConfig

infix fun String.or(that: String): String =  if (BuildConfig.DEBUG) this else that

fun String.isOnlyAlphabets() = matches("[a-zA-Z]*$".toRegex())