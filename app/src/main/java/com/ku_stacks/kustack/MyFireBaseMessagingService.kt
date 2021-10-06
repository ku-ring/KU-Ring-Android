package com.ku_stacks.kustack

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ku_stacks.kustack.ui.home.HomeActivity
import timber.log.Timber

class MyFireBaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String){
        Timber.e("refreshed token : $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            Timber.e("FCM Log Title: ${it.title}")
            Timber.e("FCM Log Body: ${it.body}")
            sendNotification(it.title, it.body)
        }
    }

    private fun sendNotification(title: String?, body: String?){
        val intent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = "ku_stack_channel_id"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())

    }
}