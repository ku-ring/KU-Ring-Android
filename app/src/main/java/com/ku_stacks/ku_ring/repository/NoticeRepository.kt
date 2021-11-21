package com.ku_stacks.ku_ring.repository

import androidx.paging.*
import androidx.paging.rxjava3.flowable
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.source.NoticePagingSource
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.PreferenceUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val pref: PreferenceUtil
) {
    private val isNewRecordHashMap = HashMap<String, NoticeEntity>()

    fun getNotices(type: String): Flowable<PagingData<Notice>> {
        return getSingleLocal()
            .flatMap { getFlowableRemoteNotice(type) }
            .map { transformRemoteData(it, type) }
    }

    private fun transformRemoteData(pagingData: PagingData<Notice>, type: String): PagingData<Notice> {
        val startDate = pref.startDate
        val subscribingSet = pref.subscription
        val isSubscribing = subscribingSet?.contains(type) == true

        if (startDate.isNullOrEmpty() || DateUtil.isToday(startDate)) { // 설치 이후 앱을 처음 킨 경우
            Timber.e("This is first connect day")
            if (startDate.isNullOrEmpty()) {
                pref.startDate = DateUtil.getToday()
            }
            return pagingData.map {
                val isRead = noticeDao.isReadNotice(it.articleId)
                Notice(
                    postedDate = it.postedDate,
                    subject = it.subject,
                    category = it.category,
                    url = it.url,
                    articleId = it.articleId,
                    isNew = DateUtil.isToday(it.postedDate) && !isRead,
                    isRead = isRead,
                    isSubscribing = isSubscribing,
                    tag = it.tag
                )
            }
        } else { //앱을 처음 킨 것이 아닌 경우(일반적인 케이스)
            Timber.e("This is not first connect day")
            return pagingData.map {
                Notice(
                    postedDate = it.postedDate,
                    subject = it.subject,
                    category = it.category,
                    url = it.url,
                    articleId = it.articleId,
                    isNew = it.postedDate > startDate && !isNewRecordHashMap.containsKey(it.articleId),
                    isRead = noticeDao.isReadNotice(it.articleId),
                    isSubscribing = isSubscribing,
                    tag = it.tag
                )
            }
        }
    }

    private fun getSingleLocal(): Flowable<List<NoticeEntity>> {
        return noticeDao.getNoticeRecord()
            .subscribeOn(Schedulers.io())
            .toFlowable()
            .doOnNext { // local 데이터가 처음 발행될때 HashMap 에 저장 (단 한번만 실행)
                if(isNewRecordHashMap.size == 0){
                    for(localNotice in it){
                        isNewRecordHashMap[localNotice.articleId] = localNotice
                    }
                }
            }
    }

    //not using now
    private fun getFlowableLocal(): Flowable<List<NoticeEntity>> {
        return noticeDao.getReadNoticeRecord(true) // isRead에 대해 실시간으로 발행
            .distinctUntilChanged()
    }

    private fun getFlowableRemoteNotice(type: String): Flowable<PagingData<Notice>>{
         return Pager(
            config = PagingConfig(
                pageSize = 20,  //이것보다 PagingSource 에서 ItemCount 가 중요함
                enablePlaceholders = true
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient.noticeService) }
        ).flowable
    }

    fun insertNotice(articleId: String, category: String): Completable {
        return noticeDao.insertNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isNew = false,
                isRead = false)
        )
    }

    fun updateNoticeToBeRead(articleId: String, category: String): Completable {
        return noticeDao.updateNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isNew = false,
                isRead = true)
        )
    }

    fun deleteAllNoticeRecord() { // for testing
        noticeDao.deleteAllNoticeRecord()
            .subscribeOn(Schedulers.io())
            .subscribe ({
                Timber.e("delete db success")
            }, {
                Timber.e("delete db fail")
            })
    }

    fun deleteSharedPreference() { //for testing
        pref.deleteStartDate()
    }
}