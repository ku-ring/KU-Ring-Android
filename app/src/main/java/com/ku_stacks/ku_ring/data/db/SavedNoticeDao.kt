package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedNoticeDao {

    @Query("SELECT * FROM SavedNoticeEntity")
    fun getAllSavedNotices(): Flow<List<SavedNoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNotice(notice: SavedNoticeEntity)

    @Delete
    suspend fun deleteNotice(notice: SavedNoticeEntity)

    @Query("DELETE from SavedNoticeEntity WHERE articleId = :articleId")
    suspend fun deleteNotice(articleId: String)

    @Query("DELETE from SavedNoticeEntity")
    suspend fun deleteAllSavedNotices()
}