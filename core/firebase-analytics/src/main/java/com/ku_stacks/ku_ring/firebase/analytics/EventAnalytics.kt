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

@SuppressLint("MissingPermission")
class EventAnalytics(
    @param:ApplicationContext private val context: Context,
    @param:AmplitudeApiKey private val amplitudeApiKey: String,
) {
    private val delegate by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    private val amplitude: Amplitude? by lazy {
        amplitudeApiKey.takeIf(String::isNotBlank)?.let { apiKey ->
            Amplitude(
                Configuration(
                    apiKey = apiKey,
                    context = context.applicationContext,
                    minIdLength = 1,
                    autocapture = setOf(
                        AutocaptureOption.SESSIONS,
                        AutocaptureOption.APP_LIFECYCLES,
                    ),
                ),
            )
        }
    }

    private fun logEvent(name: String, params: Bundle.() -> Unit) {
        val bundle = Bundle().apply(params)
        delegate.logEvent(name, bundle)
        amplitude?.track(name, bundle.toMap())
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
        val bundle = Bundle().apply {
            event.params.forEach { (key, value) ->
                putFirebaseValue(key, value)
            }
        }
        delegate.logEvent(event.name, bundle)
        amplitude?.track(event.name, event.params)
    }

    private fun Bundle.putFirebaseValue(key: String, value: Any) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putLong(key, value.toLong())
            is Long -> putLong(key, value)
            is Float -> putDouble(key, value.toDouble())
            is Double -> putDouble(key, value)
            is Boolean -> putString(key, value.toString())
            else -> putString(key, value.toString())
        }
    }

    @Suppress("DEPRECATION")
    private fun Bundle.toMap(): Map<String, Any?> = keySet().associateWith(::get)

    companion object {
        const val KuRing_Error = "com_kuring_application_error"
        const val Log = "log"
    }
}
