package com.ku_stacks.ku_ring.firebase.messaging

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.firebase.messaging.type.NotificationType
import com.ku_stacks.ku_ring.firebase.messaging.type.NotificationType.ACADEMIC_EVENT
import com.ku_stacks.ku_ring.firebase.messaging.type.NotificationType.CLUB
import com.ku_stacks.ku_ring.firebase.messaging.type.NotificationType.CUSTOM
import com.ku_stacks.ku_ring.firebase.messaging.type.NotificationType.NOTICE
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.work.RegisterUserWork
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class KuringMessagingService : FirebaseMessagingService() {

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
        runCatching {
            val data = remoteMessage.data
            val notificationType = NotificationType.from(data["type"])
            val receivedDate = DateUtil.getCurrentTime()

            when (notificationType) {
                NOTICE -> onNoticeMessageReceived(data, receivedDate)
                CUSTOM -> onCustomMessageReceived(data, receivedDate)
                CLUB -> onClubMessageReceived(data, receivedDate)
                ACADEMIC_EVENT -> onAcademicEventMessageReceived(data, receivedDate)
            }
        }.onFailure { e ->
            e.printStackTrace()
        }
    }

    private fun onNoticeMessageReceived(
        data: Map<String, String?>,
        receivedDate: String,
    ) {
        if(!fcmUtil.isNoticeNotification(data)) return

        fcmUtil.insertNoticeNotificationIntoDatabase(data, receivedDate)
        showNotificationWithUrl(data)
    }

    private fun onCustomMessageReceived(
        data: Map<String, String?>,
        receivedDate: String,
    ) {
        if(!fcmUtil.isCustomNotification(data)) return

        fcmUtil.insertNotificationIntoDatabase(data, receivedDate)
        if (pref.extNotificationAllowed) {
            showCustomNotification(data)
        }
    }

    private fun onAcademicEventMessageReceived(
        data: Map<String, String?>,
        receivedDate: String,
    ) {
        if(!fcmUtil.isAcademicEventNotification(data)) return

        fcmUtil.insertNotificationIntoDatabase(data, receivedDate)
        showAcademicEventNotification(data)
    }

    private fun onClubMessageReceived(
        data: Map<String, String?>,
        receivedDate: String,
    ) {
        if(!fcmUtil.isClubNotification(data)) return

        fcmUtil.insertNotificationIntoDatabase(data, receivedDate)
        showClubNotification(data)
    }

    private fun showNotificationWithUrl(data: Map<String, String?>) {
        val id = data["id"]?.toInt() ?: 0
        val articleId = data["articleId"]!!
        val category = data["category"]!!
        val url = data["baseUrl"]!!
        val title = data["subject"]!!
        val body = WordConverter.convertEnglishToKorean(category)

        val intent = navigator.createNoticeWebIntent(this, url, articleId, id, category, title)
        KuringNotificationManager.showNotificationWithUrl(
            this, intent, title, body,
            largeIconRes = R.drawable.ic_notification,
            smallIconRes = R.drawable.ic_status_bar
        )
    }

    private fun showCustomNotification(data: Map<String, String?>) {
        val type = data["type"]!!
        val title = data["title"]!!
        val body = data["body"]!!

        val intent = navigator.createMainIntent(this)
        KuringNotificationManager.showCustomNotification(
            this, intent, type, title, body,
            largeIconRes = R.drawable.ic_notification,
            smallIconRes = R.drawable.ic_status_bar
        )
    }

    private fun showAcademicEventNotification(data: Map<String, String?>) {
        val title = data["title"]!!
        val body = data["body"]!!

        val intent = navigator.createMainIntent(this, MainScreenRoute.Calendar)
        KuringNotificationManager.showAcademicEventNotification(
            this, intent, title, body,
            largeIconRes = R.drawable.ic_notification,
            smallIconRes = R.drawable.ic_status_bar
        )
    }

    private fun showClubNotification(data: Map<String, String?>) {
        // TODO: 동아리의 상세화면 인텐트와 동아리 알림을 추가
    }
}
