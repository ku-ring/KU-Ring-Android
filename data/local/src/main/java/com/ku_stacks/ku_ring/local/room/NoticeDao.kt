package com.ku_stacks.ku_ring.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.NoticeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoticeAsOld(notice: NoticeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticeEntity)

    @Query("SELECT * FROM NoticeEntity ORDER BY isImportant")
    fun getOldNoticeList(): Flow<List<NoticeEntity>>

    @Query("SELECT articleId FROM NoticeEntity WHERE isRead = :value ORDER BY isImportant")
    fun getReadNoticeList(value: Boolean): Flow<List<String>>

    @Query("SELECT * FROM NoticeEntity WHERE isSaved = :isSaved ORDER BY isImportant")
    fun getNoticesBySaved(isSaved: Boolean): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM NoticeEntity WHERE isSaved = :isSaved ORDER BY isImportant")
    fun getSavedNoticeList(isSaved: Boolean): List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotice(notice: NoticeEntity)

    @Query("UPDATE NoticeEntity SET isRead = 1 WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeAsRead(articleId: String, category: String)

    @Query("UPDATE NoticeEntity SET isSaved = :isSaved WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeSaveState(articleId: String, category: String, isSaved: Boolean)

    @Query("UPDATE NoticeEntity SET isReadOnStorage = :isSaved WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeAsReadOnStorage(articleId: String, category: String, isSaved: Boolean)

    @Query("UPDATE NoticeEntity SET isSaved = 0")
    suspend fun clearSavedNotices()

    @Query("SELECT COUNT(*) FROM NoticeEntity WHERE isRead = :value and articleId = :id")
    suspend fun getCountOfReadNotice(value: Boolean, id: String): Int

    suspend fun isReadNotice(id: String): Boolean {
        return getCountOfReadNotice(true, id) > 0
    }

    // 학과별 공지 쿼리
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepartmentNotices(notices: List<NoticeEntity>)

    @Query(
        "SELECT * FROM NoticeEntity WHERE department LIKE :shortName " +
                "ORDER BY isImportant, postedDate DESC, articleId DESC"
    )
    fun getDepartmentNotices(shortName: String): PagingSource<Int, NoticeEntity>

    @Query("DELETE FROM NoticeEntity WHERE department LIKE :shortName")
    suspend fun clearDepartment(shortName: String)

    //not using now
    @Query("DELETE FROM NoticeEntity")
    suspend fun deleteAllNoticeRecord()
}