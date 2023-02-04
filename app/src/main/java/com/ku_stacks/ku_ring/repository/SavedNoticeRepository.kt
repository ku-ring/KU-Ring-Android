package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.model.SavedNotice
import kotlinx.coroutines.flow.Flow

interface SavedNoticeRepository {
    fun getSavedNotices(): Flow<List<SavedNotice>>

    suspend fun saveNotice(notice: SavedNotice)

    suspend fun deleteNotice(articleId: String)

    suspend fun clearNotices()
}