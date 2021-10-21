package com.ku_stacks.ku_ring.repository

import androidx.paging.*
import androidx.paging.rxjava3.flowable
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.source.NoticePagingSource
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
) {
    private var noticeRecordList: List<NoticeEntity>? = null

    fun getNotices(type: String): Flowable<PagingData<Notice>> {
        return getFlowableLocal()
            .flatMap { localNoticeInfo ->
                noticeRecordList = localNoticeInfo //TODO exist 검사를 위해 Hashmap 으로 저장하자
                getFlowableRemoteNotice(type)
            }
            .map {
                it.map { notice ->
                    insertNotice(notice.articleId, notice.category)
                    addUserInfoWithDB(notice)
                }
            }
    }

    private fun addUserInfoWithDB(notice: Notice): Notice {
        var _isNew = true
        var _isRead = false
        noticeRecordList?.let {
            //TODO Hashmap으로 개선 가능
            for (noticeRecord in it) {
                if (noticeRecord.articleId == notice.articleId) {
                    _isNew = false
                    _isRead = notice.isRead
                    break
                }
            }
        }
        Timber.e("articleId with full Notice Data : ${notice.articleId}")

        return Notice(
            postedDate = notice.postedDate,
            subject = notice.subject,
            category = notice.category,
            url = notice.url,
            articleId = notice.articleId,
            isRead = _isRead,
            isNew = _isNew
        )
    }

    private fun getFlowableLocal(): Flowable<List<NoticeEntity>> {
        return if (noticeRecordList != null) {
            Flowable.just(noticeRecordList)
        } else {
            noticeDao.getNoticeRecord()
                .subscribeOn(Schedulers.io())
                .toFlowable()
        }
    }

    private fun getFlowableRemoteNotice(type: String): Flowable<PagingData<Notice>>{
         return Pager(
            config = PagingConfig(
                pageSize = 10,  //이것보다 PagingSource 에서 ItemCount 가 중요함
                enablePlaceholders = true
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient.noticeService) }
        ).flowable
    }

    private fun insertNotice(articleId: String, category: String) {
        noticeDao.insertNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isRead = false,
                isNew = false)
        ).subscribeOn(Schedulers.io())
            .subscribe({ Timber.e("noticeRecord Insert true") },
                { Timber.e("noticeRecord Insert fail") })

    }
}