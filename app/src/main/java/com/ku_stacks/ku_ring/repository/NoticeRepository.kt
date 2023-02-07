package com.ku_stacks.ku_ring.repository

import androidx.paging.PagingData
import com.ku_stacks.ku_ring.data.model.Notice
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.CoroutineScope

interface NoticeRepository {
    fun getNotices(type: String, scope: CoroutineScope): Flowable<PagingData<Notice>>
    fun insertNoticeAsOld(articleId: String, category: String): Completable
    fun updateNoticeToBeRead(articleId: String): Completable
    fun deleteAllNoticeRecord()
    fun deleteSharedPreference()
}