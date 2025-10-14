package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ku_stacks.ku_ring.local.entity.NoticePageEntity

@Dao
interface NoticePageDao {
    @Upsert
    suspend fun insertAll(entities: List<NoticePageEntity>)

    @Query("SELECT * FROM NoticePageEntity WHERE articleId = :articleId")
    suspend fun getNoticePageById(articleId: String): NoticePageEntity?

    @Query("SELECT MAX(page) FROM NoticePageEntity WHERE category = :category")
    suspend fun getMaxPageByCategory(category: String): Int?

    @Query("SELECT MIN(page) FROM NoticePageEntity WHERE category = :category")
    suspend fun getMinPageByCategory(category: String): Int?

    @Query("SELECT MAX(page) FROM NoticePageEntity WHERE department = :department")
    suspend fun getMaxPageByDepartment(department: String): Int?

    @Query("SELECT MIN(page) FROM NoticePageEntity WHERE department = :department")
    suspend fun getMinPageByDepartment(department: String): Int?

    @Query("DELETE FROM NoticePageEntity WHERE category = :category")
    suspend fun clearByCategory(category: String)

    @Query("DELETE FROM NoticePageEntity WHERE department = :department")
    suspend fun clearByDepartment(department: String)
}