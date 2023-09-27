package com.ku_stacks.ku_ring.remote.util

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(@ApplicationContext context: Context) : Interceptor {

    private val packageName = context.packageName
    private val kuringVersion = context.packageManager?.getPackageInfo(packageName, 0)?.versionName

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().addVersionHeader().build()
        return chain.proceed(newRequest)
    }

    private fun Request.Builder.addVersionHeader(): Request.Builder {
        val kuringVersionHeader = "Kuring/$kuringVersion"

        val androidVersion = Build.VERSION.SDK_INT.toString() // 33
        val androidVersionHeader = "Android/$androidVersion"

        Timber.d("$androidVersionHeader, $kuringVersionHeader")
        addHeader("User-Agent", kuringVersionHeader)
        addHeader("User-Agent", androidVersionHeader)
        return this
    }
}