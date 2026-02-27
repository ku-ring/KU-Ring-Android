package com.ku_stacks.ku_ring.firebase.messaging

import com.ku_stacks.ku_ring.firebase.messaging.mapper.getNoticeEntity
import com.ku_stacks.ku_ring.firebase.messaging.mapper.getPushEntity
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.local.room.PushDao
import com.ku_stacks.ku_ring.util.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
        data: Map<String, String?>,
        receivedDate: String,
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val pushEntity = getPushEntity(data, receivedDate)
                val noticeEntity = getNoticeEntity(data)
                pushDao.insertNotification(pushEntity)
                noticeDao.insertNotice(noticeEntity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun insertNotificationIntoDatabase(
        data: Map<String, String?>,
        receivedDate: String,
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                val entity = getPushEntity(data, receivedDate)
                pushDao.insertNotification(entity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
