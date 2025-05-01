package com.ku_stacks.ku_ring.ui_util

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(msg: String) =
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

fun Context.showToast(@StringRes id: Int) = showToast(getString(id))

fun Context.getAppVersionName(): String {
    // Deprecated되지 않은 다른 함수가 API 33 이상에서만 사용할 수 있어서 부득이하게 deprecated 함수를 사용
    return try {
        val info = this.packageManager?.getPackageInfo(this.packageName, 0)
        info?.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    } ?: ""
}
