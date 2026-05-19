package com.ku_stacks.ku_ring.util

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class KuringNotificationManagerTest {

    @Test
    fun `showCustomNotification sets body as big text`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val body = "오늘 19:30 녹색지대 공연 시작\n\nHearts2Hearts\nJANNABI\nZICO"

        KuringNotificationManager.showCustomNotification(
            context = context,
            intent = Intent(),
            title = "녹색지대 DAY1",
            body = body,
            largeIconRes = 0,
            smallIconRes = 0,
        )

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = shadowOf(notificationManager).allNotifications.single()

        assertThat(
            notification.extras.getCharSequence(Notification.EXTRA_BIG_TEXT).toString(),
            `is`(body),
        )
    }
}
