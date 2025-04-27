package com.ku_stacks.ku_ring.thirdparty.firebase


import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.entity.PushEntity
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

    fun insertNotificationIntoDatabase(
        articleId: String,
        id: Int,
        category: String,
        postedDate: String,
        subject: String,
        fullUrl: String,
        receivedDate: String
    ) {
        CoroutineScope(ioDispatcher).launch {
            try {
                pushDao.insertNotification(
                    PushEntity(
                        articleId = articleId,
                        category = category,
                        postedDate = postedDate,
                        subject = subject,
                        fullUrl = fullUrl,
                        isNew = true,
                        receivedDate = receivedDate
                    )
                )
                noticeDao.insertNotice(
                    NoticeEntity(
                        articleId = articleId,
                        id = id,
                        category = category,
                        subject = subject,
                        postedDate = postedDate,
                        url = fullUrl,
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
}
