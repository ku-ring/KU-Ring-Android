package com.ku_stacks.ku_ring.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext

class EventAnalytics(@ApplicationContext context: Context) {

    private val delegate by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    private fun logEvent(name: String, params: Bundle.() -> Unit){
        delegate.logEvent(name, Bundle().apply{ params() })
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

    companion object {
        const val KuRing_Error = "com_kuring_application_error"
        const val Log = "log"
    }
}