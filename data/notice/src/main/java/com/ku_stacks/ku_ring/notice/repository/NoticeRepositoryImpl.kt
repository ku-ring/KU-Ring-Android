package com.ku_stacks.ku_ring.notice.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.flowable
import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import com.ku_stacks.ku_ring.local.room.NoticeDao
import com.ku_stacks.ku_ring.notice.mapper.toEntity
import com.ku_stacks.ku_ring.notice.mapper.toNotice
import com.ku_stacks.ku_ring.notice.mapper.toNoticeList
import com.ku_stacks.ku_ring.notice.source.DepartmentNoticeMediator
import com.ku_stacks.ku_ring.notice.source.NoticePagingSource
import com.ku_stacks.ku_ring.preferences.PreferenceUtil
import com.ku_stacks.ku_ring.remote.notice.NoticeClient
import com.ku_stacks.ku_ring.remote.notice.request.SubscribeRequest
import com.ku_stacks.ku_ring.util.DateUtil
import com.ku_stacks.ku_ring.util.IODispatcher
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class NoticeRepositoryImpl @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val pref: PreferenceUtil,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
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
        val flowableLocal = noticeDao.getReadNoticeList(true)
            .distinctUntilChanged { old, new ->
                /**
                DB insert 되는 경우, 업데이트를 감지하기 위함이므로 성능을 위해
                모든 내용을 비교하기 보다는 size 만 비교하는 것으로 재정의함.
                 */
                old.size == new.size
            }

        val savedPublisherLocal = noticeDao.getNoticesBySaved(true)
            .map { notices -> notices.map { it.articleId } }
            .asPublisher(scope.coroutineContext)

        return Flowable.combineLatest(
            flowableRemote,
            flowableLocal,
            savedPublisherLocal,
        ) { remotePagingData, localNoticeData, savedArticleIds -> //isRead 의 변동이 있을때 알맞게 변형시켜야함
            remotePagingData.map { notice ->
                notice.copy(
                    isRead = localNoticeData.contains(notice.articleId),
                    isSaved = savedArticleIds.contains(notice.articleId),
                )
            }
        }
    }

    private fun transformRemoteData(
        pagingData: PagingData<Notice>,
        type: String,
    ): PagingData<Notice> {
        val startDate = pref.startDate
        val subscribingSet = pref.subscription
        val isSubscribing = subscribingSet.contains(type)

        if (startDate.isNullOrEmpty() || DateUtil.isToday(startDate)) {
            /** 설치 이후 앱을 처음 킨 경우 */
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
                if (isNewRecordHashMap.size == 0) {
                    for (localNotice in it) {
                        isNewRecordHashMap[localNotice.articleId] = localNotice
                    }
                }
            }
    }

    private fun getFlowableRemoteNotice(type: String): Flowable<PagingData<Notice>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                /** 이것보다 PagingSource 에서 ItemCount 가 중요함 */
                enablePlaceholders = true
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient) }
        ).flowable
    }

    override suspend fun insertNotice(notice: Notice) {
        noticeDao.insertNotice(notice.toEntity())
    }

    override fun insertNoticeAsOld(notice: Notice): Completable {
        return noticeDao.insertNoticeAsOld(notice.copy(isNew = false).toEntity())
    }

    override fun getSavedNotices(): Flow<List<Notice>> {
        return noticeDao.getNoticesBySaved(true).map {
            it.toNoticeList().sortedByDescending { notice -> notice.postedDate }
        }
    }

    override fun updateNoticeToBeRead(articleId: String, category: String): Completable {
        return noticeDao.updateNoticeAsRead(articleId, category)
    }

    override suspend fun updateSavedStatus(articleId: String, category: String, isSaved: Boolean) {
        withContext(ioDispatcher) {
            noticeDao.updateNoticeSaveState(articleId, category, isSaved)
            if (!isSaved) {
                noticeDao.updateNoticeAsReadOnStorage(articleId, category, false)
            }
        }
    }

    override suspend fun updateNoticeToBeReadOnStorage(articleId: String, category: String) {
        withContext(ioDispatcher) {
            noticeDao.updateNoticeAsReadOnStorage(articleId, category, true)
        }
    }

    override suspend fun clearSavedNotices() {
        withContext(ioDispatcher) {
            noticeDao.clearSavedNotices()
        }
    }

    override fun deleteAllNoticeRecord() { // for testing
        noticeDao.deleteAllNoticeRecord()
            .subscribeOn(Schedulers.io())
            .subscribe({ }, { })
    }

    override fun deleteSharedPreference() { //for testing
        pref.deleteStartDate()
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getDepartmentNotices(shortName: String): Flow<PagingData<Notice>> {
        val pagingSourceFactory = { noticeDao.getDepartmentNotices(shortName) }
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true,
            ),
            remoteMediator = DepartmentNoticeMediator(
                shortName,
                noticeClient,
                noticeDao,
                pref,
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { noticeEntityPagingData -> noticeEntityPagingData.map { it.toNotice() } }
    }

    override fun fetchSubscriptionFromRemote(token: String): Single<List<String>> {
        return noticeClient.fetchSubscribe(token)
            .map { response ->
                response.categoryList.map { it.koreanName }
            }
    }

    override fun saveSubscriptionToRemote(token: String, subscribeCategories: List<String>) {
        val subscribeRequest = SubscribeRequest(subscribeCategories)
        noticeClient.saveSubscribe(token, subscribeRequest)
            .subscribeOn(Schedulers.io())
            .subscribe({ response ->
                if (response.isSuccess) {
                    pref.firstRunFlag = false
                }
            }, { })
    }

    override suspend fun getNoticeSearchResult(query: String): List<Notice> =
        withContext(Dispatchers.IO) {
            val result = noticeClient.fetchNoticeList(query)
                .takeIf { it.isSuccess }
                ?.toNoticeList() ?: emptyList()

            val savedArticleIdSet = noticeDao.getSavedNoticeList(true).map { it.articleId }.toSet()

            result.map {
                it.copy(isSaved = savedArticleIdSet.contains(it.articleId))
            }
        }

    companion object {
        private const val pageSize = 20
    }
}
