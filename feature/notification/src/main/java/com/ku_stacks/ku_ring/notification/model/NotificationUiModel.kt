package com.ku_stacks.ku_ring.notification.model

import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_calendar_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_club_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_list_v2
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.domain.NotificationCategory
import com.ku_stacks.ku_ring.domain.NotificationContent
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_academic
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_club
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_custom
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_category_notice
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_received_days_ago
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_received_months_ago
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_received_today
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_received_weeks_ago
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_received_year_ago
import com.ku_stacks.ku_ring.feature.notification.R.string.notification_received_yesterday
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class NotificationUiModel(
    val notification: Notification,
) {
    val iconRes = when (notification.category) {
        NotificationCategory.ACADEMIC_EVENT -> ic_calendar_v2
        NotificationCategory.CLUB -> ic_club_v2
        else -> ic_list_v2
    }

    val categoryStringRes = when (notification.category) {
        NotificationCategory.NOTICE -> notification_category_notice
        NotificationCategory.ACADEMIC_EVENT -> notification_category_academic
        NotificationCategory.CLUB -> notification_category_club
        else -> notification_category_custom
    }

    val content = when (notification.content) {
        is NotificationContent.Notice -> (notification.content as NotificationContent.Notice).subject
        is NotificationContent.Club -> (notification.content as NotificationContent.Club).title
        is NotificationContent.Common -> (notification.content as NotificationContent.Common).title
    }

    val daysSinceReceived = getDaysSinceReceived(notification.receivedDate)

    private fun getDaysSinceReceived(receivedDate: String): DaysSinceReceived? {
        val days = getDaysAgo(receivedDate) ?: return null

        return when (days) {
            0L -> DaysSinceReceived(notification_received_today, 0)
            1L -> DaysSinceReceived(notification_received_yesterday, 1)
            in 2 until 7 -> DaysSinceReceived(notification_received_days_ago, days)
            in 7 until 31 -> DaysSinceReceived(notification_received_weeks_ago, days / 7)
            in 31 until 365 -> DaysSinceReceived(notification_received_months_ago, days / 30)
            else -> DaysSinceReceived(notification_received_year_ago, days / 365)
        }
    }

    private fun getDaysAgo(
        dateString: String,
        now: Instant = Instant.now(),
    ): Long? {
        if (dateString.isBlank()) return null

        return try {
            val localDateTime = LocalDateTime.parse(dateString, formatter)
            val targetInstant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
            ChronoUnit.DAYS.between(targetInstant, now)
        } catch (_: Exception) {
            null
        }
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
    }
}

data class DaysSinceReceived(
    val stringRes: Int,
    val days: Long,
)
