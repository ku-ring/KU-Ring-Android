package com.ku_stacks.ku_ring.work

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ku_stacks.ku_ring.MyFireBaseMessagingService
import com.ku_stacks.ku_ring.R
import com.ku_stacks.ku_ring.ui.main.MainActivity

class ReengagementNotificationWork(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val notification = createNotification(applicationContext)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
        return Result.success()
    }

    private fun createNotification(context: Context): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val resources = context.resources
        return NotificationCompat.Builder(context, MyFireBaseMessagingService.CHANNEL_ID)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
            .setSmallIcon(R.drawable.ic_status_bar)
            .setContentTitle(resources.getText(R.string.reengagement_title))
            .setContentText(resources.getText(R.string.reengagement_body))
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    companion object {
        val WORK_NAME = "notification_work"
    }
}