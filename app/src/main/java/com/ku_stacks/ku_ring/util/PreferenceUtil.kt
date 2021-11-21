package com.ku_stacks.ku_ring.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

class PreferenceUtil(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ku_ring_prefs", Context.MODE_PRIVATE)

    var startDate: String?
        get() = prefs.getString(START_DATE, "")
        set(value) = prefs.edit().putString(START_DATE, value).apply()

    var fcmToken: String?
        get() = prefs.getString(FCM_TOKEN, "")
        set(value) = prefs.edit().putString(FCM_TOKEN, value).apply()

    var subscription: Set<String>?
        get() = prefs.getStringSet(SUBSCRIPTION, emptySet())
        set(stringSet) = prefs.edit().putStringSet(SUBSCRIPTION, stringSet).apply()

    fun deleteStartDate() {
        prefs.edit().remove(START_DATE).apply()
    }

    companion object {
        const val START_DATE = "START_DATE"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val SUBSCRIPTION = "SUBSCRIPTION"
    }
}