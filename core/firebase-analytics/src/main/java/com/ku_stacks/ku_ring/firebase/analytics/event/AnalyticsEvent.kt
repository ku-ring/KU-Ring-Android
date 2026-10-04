package com.ku_stacks.ku_ring.firebase.analytics.event

class AnalyticsEvent private constructor(
    val name: String,
    val params: Map<String, Any> = emptyMap(),
) {
    companion object {
        val noticeHomeView = AnalyticsEvent(name = "notice_home_view")

        fun noticeTopTabSelect(tabName: String) = AnalyticsEvent(
            name = "notice_top_tab_select",
            params = mapOf("tab_name" to tabName),
        )

        fun noticeItemClick(
            noticeTitle: String,
            noticeId: Int,
            articleId: String,
            postedDate: String,
        ) = AnalyticsEvent(
            name = "notice_item_click",
            params = mapOf(
                "notice_title" to noticeTitle,
                "notice_id" to noticeId,
                "article_id" to articleId,
                "posted_date" to postedDate,
            ),
        )

        fun noticeHeaderIconClick(icon: String) = AnalyticsEvent(
            name = "header_icon_click",
            params = mapOf("icon" to icon),
        )

        fun calendarView(displayedMonth: String) = AnalyticsEvent(
            name = "calendar_view",
            params = mapOf("displayed_month" to displayedMonth),
        )

        fun calendarMonthNavigate(
            direction: String,
            displayedMonth: String,
        ) = AnalyticsEvent(
            name = "month_navigate",
            params = mapOf(
                "direction" to direction,
                "displayed_month" to displayedMonth,
            ),
        )

        fun calendarDateClick(
            date: String,
            hasEvent: Boolean,
            eventCount: Int,
        ) = AnalyticsEvent(
            name = "date_click",
            params = mapOf(
                "date" to date,
                "has_event" to hasEvent,
                "event_count" to eventCount,
            ),
        )

        val mapView = AnalyticsEvent(name = "map_view")

        fun mapCategoryChipClick(
            category: String,
            isSelected: Boolean,
            selectedCategoryCount: Int,
        ) = AnalyticsEvent(
            name = "category_chip_click",
            params = mapOf(
                "category" to category,
                "is_selected" to isSelected,
                "selected_category_count" to selectedCategoryCount,
            ),
        )

        fun mapPinClick(buildingName: String) = AnalyticsEvent(
            name = "pin_click",
            params = mapOf("building_name" to buildingName),
        )

        fun mapSearchClick(keyword: String, action: String) = AnalyticsEvent(
            name = "search_click",
            params = mapOf(
                "keyword" to keyword,
                "action" to action,
            ),
        )

        val mapSeatStatusClick = AnalyticsEvent(name = "seat_status_click")

        val pushSettingView = AnalyticsEvent(name = "push_setting_view")
        val pushSettingDepartmentView = AnalyticsEvent(name = "push_setting_department")
        val pushSettingGeneralView = AnalyticsEvent(name = "push_setting_general")

        fun pushSettingDepartmentSelect(departmentName: String) = AnalyticsEvent(
            name = "department_select",
            params = mapOf("department_name" to departmentName),
        )

        val pushSettingDepartmentEditClick = AnalyticsEvent(name = "department_edit_click")

        fun pushSettingTabSwitch(tabName: String) = AnalyticsEvent(
            name = "tab_switch",
            params = mapOf("tab_name" to tabName),
        )

        fun pushSettingCategoryToggle(
            categoryName: String,
            enabled: Boolean,
        ) = AnalyticsEvent(
            name = "category_toggle",
            params = mapOf(
                "category_name" to categoryName,
                "enabled" to enabled,
            ),
        )

        fun pushSettingDoneClick(selectedCategoryCount: Int) = AnalyticsEvent(
            name = "done_click",
            params = mapOf("selected_category_count" to selectedCategoryCount),
        )

        fun clubTabView(selectedCategory: String) = AnalyticsEvent(
            name = "club_tab_view",
            params = mapOf("selected_category" to selectedCategory),
        )

        fun clubCategoryTabSelect(selectedCategory: String) = AnalyticsEvent(
            name = "club_category_tab_select",
            params = mapOf("selected_category" to selectedCategory),
        )

        fun clubFilterSelect(
            selectedFilter: String,
            isSelected: Boolean,
        ) = AnalyticsEvent(
            name = "club_filter_select",
            params = mapOf(
                "selected_filter" to selectedFilter,
                "is_selected" to isSelected,
            ),
        )

        fun clubSortSelect(sortOption: String) = AnalyticsEvent(
            name = "club_sort_select",
            params = mapOf("sort_option" to sortOption),
        )

        val botView = AnalyticsEvent(name = "bot_view")
    }
}
