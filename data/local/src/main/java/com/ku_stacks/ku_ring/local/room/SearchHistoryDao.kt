package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM SearchHistoryEntity WHERE keyword = :keyword LIMIT 1")
    suspend fun getEntityOrNull(keyword: String): SearchHistoryEntity?

    @Delete
    suspend fun delete(entity: SearchHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SearchHistoryEntity)

    @Query("SELECT keyword FROM SearchHistoryEntity ORDER BY id DESC")
    fun getAllSearchHistory(): Flow<List<String>>

    @Query("DELETE FROM SearchHistoryEntity")
    suspend fun deleteAll()
}
