package com.ku_stacks.ku_ring.util

import com.ku_stacks.ku_ring.data.db.PushDao
import com.ku_stacks.ku_ring.data.db.PushEntity
import com.ku_stacks.ku_ring.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FcmUtil @Inject constructor(
    private val pushDao: PushDao,
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
                Timber.e("insert notification success")
            } catch (e: Exception) {
                Timber.e("insert notification error : $e")
            }
        }
    }
}
