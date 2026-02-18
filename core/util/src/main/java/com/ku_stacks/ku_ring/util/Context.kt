package com.ku_stacks.ku_ring.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes
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

fun Context.navigateToExternalBrowser(url: String) = try {
    navigateToExternalBrowserOrThrow(url)
} catch (e: ActivityNotFoundException) {
    Timber.e(e)
}

fun Context.navigateToExternalBrowserOrThrow(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
}

fun Context.showToast(msg: String) =
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

fun Context.showToast(@StringRes id: Int) = showToast(getString(id))

fun Context.getAppVersionName(): String {
    return try {
        val info = this.packageManager?.getPackageInfo(this.packageName, 0)
        info?.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    } ?: ""
}