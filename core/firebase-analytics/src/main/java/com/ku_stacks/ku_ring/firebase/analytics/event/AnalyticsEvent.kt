package com.ku_stacks.ku_ring.firebase.analytics.event

class AnalyticsEvent private constructor(val name: String) {
    companion object {
        val noticeHomeView = AnalyticsEvent(name = "notice_home_view")
        val calendarView = AnalyticsEvent(name = "calendar_view")
        val mapView = AnalyticsEvent(name = "map_view")
        val clubTabView = AnalyticsEvent(name = "club_tab_view")
        val pushSettingView = AnalyticsEvent(name = "push_setting_view")
        val botView = AnalyticsEvent(name = "bot_view")
    }
}
