package com.ku_stacks.ku_ring.firebase.messaging

import com.ku_stacks.ku_ring.firebase.messaging.mapper.toNoticeEntity
import com.ku_stacks.ku_ring.firebase.messaging.mapper.toPushEntity
import com.ku_stacks.ku_ring.firebase.messaging.model.KuringFcmPayload
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.util.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FcmUtil @Inject constructor(
    private val pushDao: PushDao,
    private val noticeDao: NoticeDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    fun isNoticeNotification(data: Map<String, String?>): Boolean {
        val articleId = data["articleId"]
        val categoryEng = data["category"]
        val postedDate = data["postedDate"]
        val subject = data["subject"]
        val fullUrl = data["baseUrl"]

        return articleId != null && categoryEng != null && postedDate != null
                && subject != null && fullUrl != null
    }

    fun isCustomNotification(data: Map<String, String?>): Boolean {
        val customTypeSet = setOf("admin")

        val type = data["type"]?.lowercase()
        val title = data["title"]
        val body = data["body"]

        return type != null && title != null && body != null && customTypeSet.contains(type)
    }

    fun isAcademicEventNotification(data: Map<String, String?>): Boolean {
        val title = data["title"]
        val body = data["body"]
        return title != null && body != null
    }

    fun isClubNotification(data: Map<String, String?>): Boolean {
        val clubId = data["clubId"]
        val title = data["title"]
        val body = data["body"]
        return clubId != null && title != null && body != null
    }

    fun insertNoticeNotificationIntoDatabase(
        payload: KuringFcmPayload.Notice,
        receivedDate: String,
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val pushEntity = payload.toPushEntity(receivedDate)
                val noticeEntity = payload.toNoticeEntity()
                pushDao.insertNotification(pushEntity)
                noticeDao.insertNotice(noticeEntity)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    fun insertNotificationIntoDatabase(
        payload: KuringFcmPayload,
        receivedDate: String,
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val entity = payload.toPushEntity(receivedDate)
                pushDao.insertNotification(entity)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
