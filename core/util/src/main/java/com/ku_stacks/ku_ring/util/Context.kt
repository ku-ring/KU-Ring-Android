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

/**
 * 네이버 지도에서 특정 검색어를 검색한다.
 * 기기에 네이버 지도 앱이 설치되어 있다면 앱에서 검색하고, 그렇지 않다면 웹에서 검색한다.
 *
 * @param keyword 검색할 단어
 * @param appendCampusName `true`라면 단어 앞에 `건국대학교 서울캠퍼스`를 붙임
 */
fun Context.searchFromNaverMap(
    keyword: String,
    appendCampusName: Boolean = true,
) {
    val searchKeyword = if (appendCampusName) {
        getString(R.string.naver_map_keyword_campus_name_prefix, keyword)
    } else {
        keyword
    }.percentEncode()

    try {
        // 네이버 지도 앱이 깔려있다면 맵을 실행
        val uri = getString(R.string.naver_map_app_uri, searchKeyword)
        navigateToExternalBrowserOrThrow(uri)
    } catch (_: ActivityNotFoundException) {
        // 맵이 없다면, 모바일 웹을 실행
        val url = getString(R.string.naver_map_web_url, searchKeyword)
        navigateToExternalBrowser(url)
    }
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