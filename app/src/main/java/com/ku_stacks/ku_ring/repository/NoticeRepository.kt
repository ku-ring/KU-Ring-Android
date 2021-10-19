package com.ku_stacks.ku_ring.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.api.response.NoticeListResponse
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.db.NoticeEntity
import com.ku_stacks.ku_ring.data.entity.Notice
import com.ku_stacks.ku_ring.data.source.NoticePagingSource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NoticeRepository @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao
) {
    // Sample
    fun fetchNoticeList(type: String, offset: Int, max: Int): Single<NoticeListResponse> {
        return noticeClient.fetchNotice(type, offset, max)
    }

    fun getNotices(type: String): Flowable<PagingData<Notice>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,  //이것보다 PagingSource 에서 ItemCount 가 중요함
                enablePlaceholders = true
            ),
            pagingSourceFactory = { NoticePagingSource(type, noticeClient.noticeService) }
        ).flowable
    }

    // TEST
    fun insertNotice(){
        noticeDao.insertNotice(
            NoticeEntity(
                articleId = "123",
                category = "abc",
                isRead = true,
                isUpdate = true)
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Timber.e("noticeRecord Insert true") },
                { Timber.e("noticeRecord Insert fail") }
            )
    }

    // TEST
    fun getNoticeRecord() {
        noticeDao.getNoticeList()
            .subscribe({ success ->
                Timber.e("Notice record size : ${success.size}")
                Timber.e("room getNoticeRecord : ${success[0].articleId}")
        }, { error ->
                Timber.e("room getNoticeRecord : error, $error")
        })
    }


}