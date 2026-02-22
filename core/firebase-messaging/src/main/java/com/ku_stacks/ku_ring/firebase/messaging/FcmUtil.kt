package com.ku_stacks.ku_ring.firebase.messaging

import com.ku_stacks.ku_ring.firebase.messaging.mapper.getPushEntity
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.entity.PushContent
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
                val entity = getPushEntity(data, receivedDate)
                val content = entity.content as PushContent.Notice

                pushDao.insertNotification(entity)
                noticeDao.insertNotice(
                    NoticeEntity(
                        articleId = content.articleId,
                        id = content.id,
                        category = content.category,
                        subject = content.subject,
                        postedDate = content.postedDate,
                        url = content.fullUrl,
                        isNew = true,
                        isRead = false,
                        isSaved = false,
                        isImportant = false,
                        isReadOnStorage = false
                    )
                )
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
