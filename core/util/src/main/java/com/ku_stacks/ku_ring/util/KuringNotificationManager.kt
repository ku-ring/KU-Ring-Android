package com.ku_stacks.ku_ring.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat

object KuringNotificationManager {

    fun showNotificationWithUrl(
        context: Context,
        intent: Intent,
        title: String?,
        body: String?,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ) {
        val notification = createNotification(context, intent, title, body, largeIconRes, smallIconRes)
        sendNotification(context, notification, URL_NOTIFICATION)
    }

    fun showCustomNotification(
        context: Context,
        intent: Intent,
        type: String,
        title: String,
        body: String,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ) {
        val notification = createNotification(context, intent, title, body, largeIconRes, smallIconRes)
        sendNotification(context, notification, CUSTOM_NOTIFICATION)
    }

    fun showReengagementNotification(
        context: Context,
        intent: Intent,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ) {
        val notification = createNotification(
            context = context,
            intent = intent,
            titleId = R.string.reengagement_title,
            bodyId = R.string.reengagement_body,
            largeIconRes = largeIconRes,
            smallIconRes = smallIconRes
        )
        sendNotification(context, notification, REENGAGEMENT_NOTIFICATION)
    }

    fun showAcademicEventNotification(
        context: Context,
        intent: Intent,
        title: String,
        body: String,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ) {
        val notification = createNotification(context, intent, title, body, largeIconRes, smallIconRes)
        sendNotification(context, notification, ACADEMIC_EVENT_NOTIFICATION)
    }

    fun showClubNotification(
        context: Context,
        intent: Intent,
        title: String,
        body: String,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ) {
        val notification =
            createNotification(context, intent, title, body, largeIconRes, smallIconRes)
        sendNotification(context, notification, CLUB_NOTIFICATION)
    }

    private fun createNotification(
        context: Context,
        intent: Intent,
        @StringRes titleId: Int,
        @StringRes bodyId: Int,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ): Notification {
        return createNotification(
            context = context,
            intent = intent,
            title = context.resources.getText(titleId).toString(),
            body = context.resources.getText(bodyId).toString(),
            largeIconRes = largeIconRes,
            smallIconRes = smallIconRes
        )
    }

    private fun createNotification(
        context: Context,
        intent: Intent,
        title: String?,
        body: String?,
        @DrawableRes largeIconRes: Int,
        @DrawableRes smallIconRes: Int,
    ): Notification {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = CHANNEL_ID
        return NotificationCompat.Builder(context, channelId)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    largeIconRes
                )
            )
            .setSmallIcon(smallIconRes)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun sendNotification(context: Context, notification: Notification, id: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }

    const val CHANNEL_ID = "ku_stack_channel_id"
    const val CHANNEL_NAME = "쿠링"

    private const val URL_NOTIFICATION = 0
    private const val CUSTOM_NOTIFICATION = 1
    private const val REENGAGEMENT_NOTIFICATION = 2
    private const val ACADEMIC_EVENT_NOTIFICATION = 3
    private const val CLUB_NOTIFICATION = 4
}
