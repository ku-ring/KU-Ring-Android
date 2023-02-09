package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.db.SavedNoticeDao
import com.ku_stacks.ku_ring.data.mapper.toEntity
import com.ku_stacks.ku_ring.data.mapper.toSavedNoticeList
import com.ku_stacks.ku_ring.data.model.SavedNotice
import com.ku_stacks.ku_ring.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedNoticeRepositoryImpl @Inject constructor(
    private val savedNoticeDao: SavedNoticeDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : SavedNoticeRepository {
    override fun getSavedNotices(): Flow<List<SavedNotice>> =
        savedNoticeDao.getAllSavedNotices().map { it.toSavedNoticeList() }

    override suspend fun saveNotice(notice: SavedNotice) {
        withContext(ioDispatcher) {
            savedNoticeDao.saveNotice(notice.toEntity())
        }
    }

    override suspend fun deleteNotice(articleId: String) {
        withContext(ioDispatcher) {
            savedNoticeDao.deleteNotice(articleId)
        }
    }

    override suspend fun clearNotices() {
        withContext(ioDispatcher) {
            savedNoticeDao.deleteAllSavedNotices()
        }
    }
}