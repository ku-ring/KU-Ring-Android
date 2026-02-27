package com.ku_stacks.ku_ring.notification.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.ku_stacks.ku_ring.domain.Notification
import com.ku_stacks.ku_ring.domain.NotificationCategory
import com.ku_stacks.ku_ring.domain.NotificationContent

class NotificationPreviewParameterProvider : PreviewParameterProvider<Notification> {
    val notification = Notification(
        id = 1,
        category = NotificationCategory.NOTICE,
        isNew = true,
        receivedDate = "3일전",
        content = NotificationContent.Notice(
            articleId = "1",
            noticeCategory = "공지",
            subject = "공지사항입니다.",
            fullUrl = "",
            postedDate = "",
        )
    )

    override val values: Sequence<Notification>
        get() = sequenceOf(
            notification,
            notification.copy(isNew = false)
        )
}

class NotificationsPreviewParameterProvider : PreviewParameterProvider<List<Notification>> {
    val notification = Notification(
        id = 1,
        category = NotificationCategory.NOTICE,
        isNew = true,
        receivedDate = "3일전",
        content = NotificationContent.Notice(
            articleId = "1",
            noticeCategory = "공지",
            subject = "공지사항입니다.",
            fullUrl = "",
            postedDate = "",
        )
    )

    override val values: Sequence<List<Notification>>
        get() = sequenceOf(
            listOf(
                notification,
                notification.copy(
                    id = 2,
                    isNew = false,
                ),
                notification.copy(
                    id = 3,
                ),
                notification.copy(
                    id = 4,
                )
            )
        )
}
