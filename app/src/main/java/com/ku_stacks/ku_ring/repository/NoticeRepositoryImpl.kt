package com.ku_stacks.ku_ring.repository

import androidx.paging.*
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.source.NoticePagingSource
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.PreferenceUtil
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val pref: PreferenceUtil
) : NoticeRepository {
    private val isNewRecordHashMap = HashMap<String, NoticeEntity>()

    override fun getNotices(type: String, scope: CoroutineScope): Flowable<PagingData<Notice>> {
        val flowableRemote = getSingleLocalNotice()
            .flatMap { getFlowableRemoteNotice(type) }
            .map { transformRemoteData(it, type) }
            .cachedIn(scope)

        /**
         하나의 insert에 대해서 2개 또는 3개의 변화 감지가 발생할 것임.
         그 이유는 양옆 fragment 의 viewModel 에서 호출하기 때문
        */
        val flowableLocal = noticeDao.getReadNoticeRecord(true)
            .distinctUntilChanged { old, new ->
                /**
                 DB insert 되는 경우, 업데이트를 감지하기 위함이므로 성능을 위해
                 모든 내용을 비교하기 보다는 size 만 비교하는 것으로 재정의함.
                */
                old.size == new.size
            }

        return Flowable.combineLatest(
            flowableRemote,
            flowableLocal,
            { remotePagingData, localNoticeData -> //isRead 의 변동이 있을때 알맞게 변형시켜야함
                remotePagingData.map { notice ->
                    notice.copy(isRead = localNoticeData.contains(notice.articleId))
                }
            })
    }

    private fun transformRemoteData(
        pagingData: PagingData<Notice>,
        type: String,
    ): PagingData<Notice> {
        val startDate = pref.startDate
        val subscribingSet = pref.subscription
        val isSubscribing = subscribingSet?.contains(type) == true

        if (startDate.isNullOrEmpty() || DateUtil.isToday(startDate)) {
            /** 설치 이후 앱을 처음 킨 경우 */
            Timber.e("This is first connect day")
            if (startDate.isNullOrEmpty()) {
                pref.startDate = DateUtil.getToday()
            }
            return pagingData.map { notice ->
                notice.copy(
                    isNew = DateUtil.isToday(notice.postedDate) && !noticeDao.isReadNotice(notice.articleId),
                    isSubscribing = isSubscribing
                )
            }
        } else {
            /** 앱을 처음 킨 것이 아닌 경우(일반적인 케이스) */
            Timber.e("This is not first connect day")
            return pagingData.map { notice ->
                notice.copy(
                    isNew = notice.postedDate > startDate && !isNewRecordHashMap.containsKey(notice.articleId),
                    isSubscribing = isSubscribing
                )
            }
        }
    }

    private fun getSingleLocalNotice(): Flowable<List<NoticeEntity>> {
        return noticeDao.getOldNoticeList()
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

    private fun getFlowableRemoteNotice(type: String): Flowable<PagingData<Notice>>{
         return Pager(
            config = PagingConfig(
                pageSize = 20,  /** 이것보다 PagingSource 에서 ItemCount 가 중요함 */
                enablePlaceholders = true
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient) }
        ).flowable
    }

    override fun insertNoticeAsOld(articleId: String, category: String): Completable {
        return noticeDao.insertNoticeAsOld(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isNew = false,
                isRead = false)
        )
    }

    override fun updateNoticeToBeRead(articleId: String, category: String): Completable {
        return noticeDao.updateNotice(
            NoticeEntity(
                articleId = articleId,
                category = category,
                isNew = false,
                isRead = true)
        )
    }

    override fun deleteAllNoticeRecord() { // for testing
        noticeDao.deleteAllNoticeRecord()
            .subscribeOn(Schedulers.io())
            .subscribe ({
                Timber.e("delete Notice db success")
            }, {
                Timber.e("delete Notice db fail")
            })
    }

    override fun deleteSharedPreference() { //for testing
        pref.deleteStartDate()
    }
}