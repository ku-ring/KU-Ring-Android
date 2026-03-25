package com.ku_stacks.ku_ring.firebase.messaging

import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.firebase.messaging.model.KuringFcmPayload
import com.ku_stacks.ku_ring.navigation.KuringNavigator
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.KuringNotificationManager
import com.ku_stacks.ku_ring.util.WordConverter
import com.ku_stacks.ku_ring.work.RegisterUserWork
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class KuringMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var pref: PreferenceUtil

    @Inject
    lateinit var fcmUtil: FcmUtil

    @Inject
    lateinit var navigator: KuringNavigator

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override fun onNewToken(token: String) {
        if (pref.fcmToken != token) {
            pref.fcmToken = token
            enqueueRegisterUserWork(token)
        }
    }

    override fun handleIntent(intent: Intent?) {
        intent?.apply {
            // FCM 알림에 [notification] 헤더가 포함되어 있으면 [onMessageReceived]가 호출되지 않습니다.
            // 쿠링 안드로이드 프로젝트에선 [notification]에 포함된 데이터를 사용하지 않습니다.
            // 따라서 관련 헤더를 전부 지워줍니다.
            val temp = extras?.apply {
                remove(Constants.MessageNotificationKeys.ENABLE_NOTIFICATION)
                remove("gcm.notification.e")
            }
            replaceExtras(temp)
        }
        super.handleIntent(intent)
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
        if (remoteMessage.data.isEmpty()) return

        runCatching {
            val payload = remoteMessage.data.toFcmPayload()
            val receivedDate = DateUtil.getCurrentTime()

            when (payload) {
                is KuringFcmPayload.Notice -> onNoticeMessageReceived(payload, receivedDate)
                is KuringFcmPayload.AcademicEvent -> onAcademicEventMessageReceived(
                    payload,
                    receivedDate
                )

                is KuringFcmPayload.Custom -> onCustomMessageReceived(payload, receivedDate)
                is KuringFcmPayload.Club -> onClubMessageReceived(payload, receivedDate)
            }
        }.onFailure { e ->
            Timber.e(e)
        }
    }

    private fun onNoticeMessageReceived(
        payload: KuringFcmPayload,
        receivedDate: String,
    ) {
        if (payload !is KuringFcmPayload.Notice) return

        fcmUtil.insertNoticeNotificationIntoDatabase(payload, receivedDate)
        showNotificationWithUrl(payload)
    }

    private fun onCustomMessageReceived(
        payload: KuringFcmPayload,
        receivedDate: String,
    ) {
        if (payload !is KuringFcmPayload.Custom) return

        fcmUtil.insertNotificationIntoDatabase(payload, receivedDate)
        if (pref.extNotificationAllowed) {
            showCustomNotification(payload)
        }
    }

    private fun onAcademicEventMessageReceived(
        payload: KuringFcmPayload,
        receivedDate: String,
    ) {
        if (payload !is KuringFcmPayload.AcademicEvent) return

        fcmUtil.insertNotificationIntoDatabase(payload, receivedDate)
        showAcademicEventNotification(payload)
    }

    private fun onClubMessageReceived(
        payload: KuringFcmPayload,
        receivedDate: String,
    ) {
        if (payload !is KuringFcmPayload.Club) return

        fcmUtil.insertNotificationIntoDatabase(payload, receivedDate)
        showClubNotification(payload)
    }

    private fun showNotificationWithUrl(payload: KuringFcmPayload.Notice) {
        val context = this@KuringMessagingService
        with(payload) {
            val title = subject
            val body = WordConverter.convertEnglishToKorean(category)

            val intent =
                navigator.createNoticeWebIntent(context, baseUrl, articleId, id, category, title)
            KuringNotificationManager.showNotificationWithUrl(
                context, intent, title, body,
                largeIconRes = R.drawable.ic_notification,
                smallIconRes = R.drawable.ic_status_bar
            )
        }
    }

    private fun showCustomNotification(payload: KuringFcmPayload.Custom) {
        val context = this@KuringMessagingService
        with(payload) {
            val intent = navigator.createMainIntent(context)
            KuringNotificationManager.showCustomNotification(
                context, intent, title, body,
                largeIconRes = R.drawable.ic_notification,
                smallIconRes = R.drawable.ic_status_bar
            )
        }
    }

    private fun showAcademicEventNotification(payload: KuringFcmPayload.AcademicEvent) {
        val context = this@KuringMessagingService
        with(payload) {
            val intent = navigator.createMainIntent(context, MainScreenRoute.Calendar)
            KuringNotificationManager.showAcademicEventNotification(
                context, intent, title, body,
                largeIconRes = R.drawable.ic_notification,
                smallIconRes = R.drawable.ic_status_bar
            )
        }
    }

    private fun showClubNotification(payload: KuringFcmPayload.Club) {
        val context = this@KuringMessagingService
        with(payload) {
            val intent = navigator.createClubDetailIntent(context, clubId)
            KuringNotificationManager.showClubNotification(
                context, intent, title, body,
                largeIconRes = R.drawable.ic_notification,
                smallIconRes = R.drawable.ic_status_bar,
            )
        }
    }

    private fun Map<String, String>.toFcmPayload(): KuringFcmPayload {
        val jsonObject = JsonObject(mapValues { (_, value) -> JsonPrimitive(value) })
        return json.decodeFromJsonElement<KuringFcmPayload>(jsonObject)
    }
}
