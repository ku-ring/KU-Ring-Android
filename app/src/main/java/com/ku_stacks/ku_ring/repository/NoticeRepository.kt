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
    private var isNewRecordHashMap = HashMap<String, NoticeEntity>()
    private var isReadRecordList: List<NoticeEntity>? = null

    fun getNotices(type: String): Flowable<PagingData<Notice>> {
        return getFlowableLocal()
            .flatMap {
                getFlowableRemoteNotice(type)
            }
            .map {
                it.map { notice ->
                    insertNotice(notice.articleId, notice.category)
                    transformUiData(notice)
                }
            }
    }

    private fun transformUiData(remoteNotice: Notice): Notice {
        var _isNew = true
        var _isRead = false

        if(isNewRecordHashMap.containsKey(remoteNotice.articleId)){
            _isNew = false
        }

        isReadRecordList?.let {
            for(isReadRecord in it) {
                if(isReadRecord.articleId == remoteNotice.articleId) {
                    _isRead = isReadRecord.isRead
                    break
                }
            }
        }

        return Notice(
            postedDate = remoteNotice.postedDate,
            subject = remoteNotice.subject,
            category = remoteNotice.category,
            url = remoteNotice.url,
            articleId = remoteNotice.articleId,
            isRead = _isRead,
            isNew = _isNew
        )
    }

    private fun getFlowableLocal(): Flowable<List<NoticeEntity>> {
        return noticeDao.getNoticeRecord()
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .doOnNext {
                for(localNotice in it){
                    isNewRecordHashMap[localNotice.articleId] = localNotice
                }
            }
            .flatMap {
                noticeDao.getReadNoticeRecord(true)
            }
            .doOnNext {
                isReadRecordList = it //TODO DB 쿼리로 개선 가능
            }
            .distinctUntilChanged()
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

    fun updateNoticeToBeRead(articleId: String, category: String) {
        noticeDao.updateNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isRead = true,
                isNew = false)
        )
            .subscribeOn(Schedulers.io())
            .subscribe({ Timber.e("noticeRecord update true $articleId") },
                { Timber.e("noticeRecord update fail") })
    }

    fun deleteDB() { // for testing
        noticeDao.deleteAllNoticeRecord()
            .subscribeOn(Schedulers.io())
            .subscribe ({
                Timber.e("delete db success")
            }, {
                Timber.e("delete db fail")
            })
    }
}