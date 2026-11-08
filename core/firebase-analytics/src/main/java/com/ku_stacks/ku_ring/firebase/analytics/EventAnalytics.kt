package com.ku_stacks.ku_ring.firebase.analytics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.amplitude.android.Amplitude
import com.amplitude.android.AutocaptureOption
import com.amplitude.android.Configuration
import com.google.firebase.analytics.FirebaseAnalytics
import com.ku_stacks.ku_ring.firebase.analytics.event.AnalyticsEvent
import com.ku_stacks.ku_ring.firebase.analytics.initializer.AmplitudeApiKey
import dagger.hilt.android.qualifiers.ApplicationContext

class EventAnalytics(
    @param:ApplicationContext private val context: Context,
    @param:AmplitudeApiKey private val amplitudeApiKey: String,
) {
    private val delegate by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    private val amplitude: Amplitude? =
        amplitudeApiKey.takeIf(String::isNotBlank)?.let { apiKey ->
            createAmplitude(apiKey)
        }

    @SuppressLint("MissingPermission")
    private fun createAmplitude(apiKey: String) = Amplitude(
        Configuration(
            apiKey = apiKey,
            context = context.applicationContext,
            autocapture = setOf(
                AutocaptureOption.SESSIONS,
                AutocaptureOption.APP_LIFECYCLES,
            ),
        ),
    )

    private fun logEvent(name: String, params: Bundle.() -> Unit) {
        delegate.logEvent(name, Bundle().apply { params() })
    }

    fun click(screenName: String, screenClass: String) {
        logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    fun errorEvent(errorMsg: String, screenClass: String) {
        logEvent(KuRing_Error) {
            putString(Log, errorMsg)
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenClass)
        }
    }

    fun log(event: AnalyticsEvent) {
        delegate.logEvent(event.name, null)
        amplitude?.track(event.name)
    }

    companion object {
        const val KuRing_Error = "com_kuring_application_error"
        const val Log = "log"
    }
}
