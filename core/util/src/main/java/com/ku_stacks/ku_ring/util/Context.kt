package com.ku_stacks.ku_ring.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import timber.log.Timber

fun Context.findActivity(): AppCompatActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

fun Context.moveToKUMail() = try {
    val intent = Intent(Intent.ACTION_VIEW, "https://kumail.konkuk.ac.kr/".toUri())
    startActivity(intent)
} catch (e: ActivityNotFoundException) {
    Timber.e(e.printStackTrace().toString())
}