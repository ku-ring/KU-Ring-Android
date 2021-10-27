package com.ku_stacks.ku_ring.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

class PreferenceUtil(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ku_ring_prefs", Context.MODE_PRIVATE)

    fun setStartDate(str: String) {
        prefs.edit().putString(startDate, str).apply()
    }

    fun getStartDate(): String? {
        return prefs.getString(startDate, "")
    }

    fun setFcmToken(str: String) {
        prefs.edit().putString(fcmToken, str).apply()
    }

    fun getFcmToken(): String? {
        return prefs.getString(fcmToken, "")
    }

    fun deleteStartDate() {
        prefs.edit().remove(startDate).apply()
    }

    companion object {
        const val startDate = "START_DATE"
        const val fcmToken = "FCM_TOKEN"
    }
}