package com.ku_stacks.ku_ring

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ku_stacks.ku_ring.data.db.PushDao
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.FcmUtil
import com.ku_stacks.ku_ring.util.PreferenceUtil
import com.ku_stacks.ku_ring.util.WordConverter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFireBaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var pushDao: PushDao

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var fcmUtil: FcmUtil

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onNewToken(token: String) {
        Timber.e("refreshed token : $token")
        if (pref.fcmToken != token) {
            pref.fcmToken = token
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Notification hello")
        if (fcmUtil.isNoticeNotification(remoteMessage.data)) {
            val articleId = remoteMessage.data["articleId"]!!
            val category = remoteMessage.data["category"]!!
            val postedDate = remoteMessage.data["postedDate"]!!
            val subject = remoteMessage.data["subject"]!!
            // 키 이름은 baseUrl이지만, 실제로는 full url이 내려오고 있음
            val fullUrl = remoteMessage.data["baseUrl"]!!
            Timber.d("Notification fullUrl at firebase messaging: $fullUrl")

            // insert into db
            val receivedDate = DateUtil.getCurrentTime()
            fcmUtil.insertNotificationIntoDatabase(
                articleId = articleId,
                category = category,
                postedDate = postedDate,
                subject = subject,
                fullUrl = fullUrl,
                receivedDate = receivedDate
            )

            // show notification
            val categoryKr = WordConverter.convertEnglishToKorean(category)
            showNotificationWithUrl(
                title = subject,
                body = categoryKr,
                url = fullUrl,
                articleId = articleId,
                category = category
            )
        } else if (fcmUtil.isCustomNotification(remoteMessage.data)) {
            val type = remoteMessage.data["type"]!!
            val title = remoteMessage.data["title"]!!
            val body = remoteMessage.data["body"]!!

            if (pref.extNotificationAllowed) {
                showCustomNotification(type = type, title = title, body = body)
            }
        }
    }

    private fun showNotificationWithUrl(
        title: String?,
        body: String?,
        url: String?,
        articleId: String?,
        category: String?
    ) {
        val intent = navigator.createNoticeWebIntent(
            this,
            url,
            articleId,
            category,
        )
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = CHANNEL_ID
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
            .setSmallIcon(R.drawable.ic_status_bar)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun showCustomNotification(type: String, title: String, body: String) {
        val intent = navigator.createMainIntent(this)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = CHANNEL_ID
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notification))
            .setSmallIcon(R.drawable.ic_status_bar)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        const val CHANNEL_ID = "ku_stack_channel_id"
        const val CHANNEL_NAME = "쿠링"
    }
}
