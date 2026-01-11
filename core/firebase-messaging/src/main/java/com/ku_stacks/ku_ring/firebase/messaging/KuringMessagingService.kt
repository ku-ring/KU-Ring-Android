package com.ku_stacks.ku_ring.firebase.messaging

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.work.RegisterUserWork
import com.ku_stacks.ku_ring.designsystem.R as DesignR
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KuringMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var pushDao: PushDao

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var fcmUtil: FcmUtil

    @Inject
    lateinit var navigator: KuringNavigator

    override fun onNewToken(token: String) {
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
        when (remoteMessage.data["type"]) {
            MESSAGE_NOTICE -> onNoticeMessageReceived(remoteMessage.data)
            MESSAGE_CUSTOM -> onCustomMessageReceived(remoteMessage.data)
            MESSAGE_ACADEMIC_EVENT -> onAcademicEventMessageReceived(remoteMessage.data)
        }
    }

    private fun onNoticeMessageReceived(data: Map<String, String?>) {
        val id = data["id"]?.toInt() ?: 0
        val articleId = data["articleId"]!!
        val category = data["category"]!!
        val postedDate = data["postedDate"]!!
        val subject = data["subject"]!!
        val fullUrl = data["baseUrl"]!!

        val receivedDate = DateUtil.getCurrentTime()
        fcmUtil.insertNotificationIntoDatabase(
            articleId = articleId,
            id = id,
            category = category,
            postedDate = postedDate,
            subject = subject,
            fullUrl = fullUrl,
            receivedDate = receivedDate
        )

        val categoryKr = WordConverter.convertEnglishToKorean(category)
        showNotificationWithUrl(
            title = subject,
            body = categoryKr,
            url = fullUrl,
            articleId = articleId,
            id = id,
            category = category,
            subject = subject,
        )
    }

    private fun onCustomMessageReceived(data: Map<String, String?>) {
        val type = data["type"]!!
        val title = data["title"]!!
        val body = data["body"]!!

        if (pref.extNotificationAllowed) {
            showCustomNotification(type = type, title = title, body = body)
        }
    }

    private fun onAcademicEventMessageReceived(data: Map<String, String?>) {
        val title = data["title"]!!
        val body = data["body"]!!
        showAcademicEventNotification(title, body)
    }

    private fun showNotificationWithUrl(
        title: String?,
        body: String?,
        url: String?,
        articleId: String?,
        id: Int?,
        category: String?,
        subject: String?,
    ) {
        val intent = navigator.createNoticeWebIntent(this, url, articleId, id, category, subject)
        KuringNotificationManager.showNotificationWithUrl(
            this, intent, title, body,
            largeIconRes = DesignR.drawable.ic_notification,
            smallIconRes = DesignR.drawable.ic_status_bar
        )
    }

    private fun showCustomNotification(type: String, title: String, body: String) {
        val intent = navigator.createMainIntent(this)
        KuringNotificationManager.showCustomNotification(
            this, intent, type, title, body,
            largeIconRes = DesignR.drawable.ic_notification,
            smallIconRes = DesignR.drawable.ic_status_bar
        )
    }

    private fun showAcademicEventNotification(title: String, body: String) {
        val intent = navigator.createMainIntent(this, MainScreenRoute.Calendar)
        KuringNotificationManager.showAcademicEventNotification(
            this, intent, title, body,
            largeIconRes = DesignR.drawable.ic_notification,
            smallIconRes = DesignR.drawable.ic_status_bar
        )
    }

    companion object {
        private const val MESSAGE_NOTICE = "notice"
        private const val MESSAGE_CUSTOM = "admin"
        private const val MESSAGE_ACADEMIC_EVENT = "academic"
    }
}
