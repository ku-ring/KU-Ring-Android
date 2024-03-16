package com.ku_stacks.ku_ring.thirdparty.firebase

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.ui_util.KuringNavigator
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.work.RegisterUserWork
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
            enqueueRegisterUserWork(token)
        }
    }

    private fun enqueueRegisterUserWork(token: String) {
        val workerData = RegisterUserWork.createData(token)
        val registerUserWorkRequest = OneTimeWorkRequestBuilder<RegisterUserWork>()
            .setInputData(workerData)
            .build()
        WorkManager.getInstance(baseContext)
            .enqueue(registerUserWorkRequest)
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
                category = category,
                subject = subject,
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
        category: String?,
        subject: String?,
    ) {
        val intent = navigator.createNoticeWebIntent(this, url, articleId, category, subject)
        KuringNotificationManager.showNotificationWithUrl(this, intent, title, body)
    }

    private fun showCustomNotification(type: String, title: String, body: String) {
        val intent = navigator.createMainIntent(this)
        KuringNotificationManager.showCustomNotification(this, intent, type, title, body)
    }
}
