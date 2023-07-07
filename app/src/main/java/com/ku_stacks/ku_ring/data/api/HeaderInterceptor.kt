package com.ku_stacks.ku_ring.data.api

import android.os.Build
import com.ku_stacks.ku_ring.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().addVersionHeader().build()
        return chain.proceed(newRequest)
    }

    private fun Request.Builder.addVersionHeader(): Request.Builder {
        val kuringVersion = BuildConfig.VERSION_NAME
        val kuringVersionHeader = "Kuring/$kuringVersion"

        val androidVersion = Build.VERSION.SDK_INT.toString() // 33
        val androidVersionHeader = "Android/$androidVersion"

        Timber.d("$androidVersionHeader, $kuringVersionHeader")
        addHeader("User-Agent", kuringVersionHeader)
        addHeader("User-Agent", androidVersionHeader)
        return this
    }
}