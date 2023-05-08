package com.ku_stacks.ku_ring.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ku_stacks.ku_ring.data.api.NoticeClient
import com.ku_stacks.ku_ring.data.db.NoticeDao
import com.ku_stacks.ku_ring.data.mapper.toNotice
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.source.DepartmentNoticeMediator
import com.ku_stacks.ku_ring.util.PreferenceUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class DepartmentNoticeRepositoryImpl @Inject constructor(
    private val noticeClient: NoticeClient,
    private val noticeDao: NoticeDao,
    private val pref: PreferenceUtil,
) : DepartmentNoticeRepository {
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

    companion object {
        private const val pageSize = 20
    }
}