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
        return getFlowableLocal()
            .flatMap { getFlowableRemoteNotice(type) }
            .map { transformRemoteData(it) }
    }

    private fun transformRemoteData(pagingData: PagingData<Notice>): PagingData<Notice> {
        val startDate = pref.getStartDate()
        if (startDate.isNullOrEmpty() || DateUtil.isToday(startDate)) { // 설치 이후 앱을 처음 킨 날만
            Timber.e("This is first connect day")
            if (startDate.isNullOrEmpty()) {
                pref.setStartDate(DateUtil.getToday())
            }
            return pagingData.map {
                Notice(
                    postedDate = it.postedDate,
                    subject = it.subject,
                    category = it.category,
                    url = it.url,
                    articleId = it.articleId,
                    isNew = DateUtil.isToday(it.postedDate),
                    isRead = false
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
                    isNew = !isNewRecordHashMap.containsKey(it.articleId),
                    isRead = noticeDao.isReadNotice(it.articleId)
                )
            }
        }
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
            .flatMap { noticeDao.getReadNoticeRecord(true) }
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

    fun insertNotice(articleId: String, category: String) {
        noticeDao.insertNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isNew = false,
                isRead = false)
        ).subscribeOn(Schedulers.io())
            .subscribe({
                //Timber.e("noticeRecord Insert true : $articleId")
            }, { Timber.e("noticeRecord Insert fail") })
    }

    fun updateNoticeToBeRead(articleId: String, category: String) {
        noticeDao.updateNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isNew = false,
                isRead = true)
        )
            .subscribeOn(Schedulers.io())
            .subscribe({
                //Timber.e("noticeRecord update true : $articleId")
            }, { Timber.e("noticeRecord update fail") })
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