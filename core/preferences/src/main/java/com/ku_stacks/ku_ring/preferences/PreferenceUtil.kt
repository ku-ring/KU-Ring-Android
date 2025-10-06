package com.ku_stacks.ku_ring.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.ku_stacks.ku_ring.util.WordConverter
import dagger.hilt.android.qualifiers.ApplicationContext

class PreferenceUtil(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ku_ring_prefs", Context.MODE_PRIVATE)

    var firstRunFlag: Boolean
        get() = prefs.getBoolean(FIRST_RUN, true)
        set(value) = prefs.edit { putBoolean(FIRST_RUN, value) }

    var startDate: String?
        get() = prefs.getString(START_DATE, "")
        set(value) = prefs.edit { putString(START_DATE, value) }

    var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, "") ?: ""
        set(value) = prefs.edit { putString(ACCESS_TOKEN, value) }

    var fcmToken: String
        get() = prefs.getString(FCM_TOKEN, "") ?: ""
        set(value) = prefs.edit { putString(FCM_TOKEN, value) }

    var subscription: Set<String>
        get() = prefs.getStringSet(SUBSCRIPTION, emptySet()) ?: emptySet()
        private set(stringSet) = prefs.edit { putStringSet(SUBSCRIPTION, stringSet) }

    var extNotificationAllowed: Boolean
        get() = prefs.getBoolean(DEFAULT_NOTIFICATION, true)
        set(value) = prefs.edit { putBoolean(DEFAULT_NOTIFICATION, value) }

    var academicEventNotificationAllowed: Boolean
        get() = prefs.getBoolean(ACADEMIC_EVENT_NOTIFICATION, true)
        set(value) = prefs.edit { putBoolean(ACADEMIC_EVENT_NOTIFICATION, value) }

    var campusUserId: String
        get() = prefs.getString(CAMPUS_USER_ID, null) ?: ""
        set(value) = prefs.edit { putString(CAMPUS_USER_ID, value) }

    var is2024SurveyComplete: Boolean
        get() = prefs.getBoolean(SURVEY_2024_COMPLETE, false)
        set(value) = prefs.edit { putBoolean(SURVEY_2024_COMPLETE, value) }

    var lastDateAcademicEventShown: String
        get() = prefs.getString(LAST_DATE_ACADEMIC_EVENT_SHEET_SHOWN, null) ?: ""
        set(value) = prefs.edit { putString(LAST_DATE_ACADEMIC_EVENT_SHEET_SHOWN, value).apply() }

    fun deleteStartDate() {
        prefs.edit { remove(START_DATE) }
    }

    fun deleteAccessToken() {
        prefs.edit { remove(ACCESS_TOKEN) }
    }

    fun saveSubscriptionFromKorean(koreanDepartmentNames: List<String>) {
        val stringSet = koreanDepartmentNames.map {
            WordConverter.convertKoreanToShortEnglish(it)
        }.toSet()

        subscription = stringSet
    }

    companion object {
        const val FIRST_RUN = "FIRST_RUN"
        const val START_DATE = "START_DATE"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val SUBSCRIPTION = "SUBSCRIPTION"
        const val DEFAULT_NOTIFICATION = "DEFAULT_NOTIFICATION"
        const val ACADEMIC_EVENT_NOTIFICATION = "ACADEMIC_EVENT_NOTIFICATION"
        const val CAMPUS_USER_ID = "CAMPUS_USER_ID"
        const val SURVEY_2024_COMPLETE = "SURVEY_2024_COMPLETE"
        const val LAST_DATE_ACADEMIC_EVENT_SHEET_SHOWN = "LAST_DATE_ACADEMIC_EVENT_SHEET_SHOWN"
    }
}