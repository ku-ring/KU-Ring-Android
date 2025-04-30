package com.ku_stacks.ku_ring.remote.util

import android.content.Context
import android.os.Build
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(@ApplicationContext context: Context) : Interceptor {
    private val preferences = PreferenceUtil(context)

    private val packageName = context.packageName
    private val kuringVersion = context.packageManager?.getPackageInfo(packageName, 0)?.versionName

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addVersionHeader()
            .addFcmTokenHeader()
            .addAccessTokenHeader()
            .build()
        return chain.proceed(newRequest)
    }

    private fun Request.Builder.addVersionHeader(): Request.Builder {
        val kuringVersionHeader = "Kuring/$kuringVersion"

        val androidVersion = Build.VERSION.SDK_INT.toString() // 33
        val androidVersionHeader = "Android/$androidVersion"

        addHeader("User-Agent", kuringVersionHeader)
        addHeader("User-Agent", androidVersionHeader)
        return this
    }

    private fun Request.Builder.addFcmTokenHeader(): Request.Builder {
        // TODO: remove all User-Token header from each API
        removeHeader("User-Token")

        val fcmToken = preferences.fcmToken
        addHeader("User-Token", fcmToken)
        return this
    }

    private fun Request.Builder.addAccessTokenHeader(): Request.Builder {
        val accessToken = preferences.accessToken
        if (accessToken.isNotBlank()) addHeader("Authorization", "Bearer $accessToken")
        return this
    }
}