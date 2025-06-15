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

    // TODO: 하단 getDepartmentNotices의 use case를 모두 이 함수로 대체하기 (이름만 다른 동일한 함수임)
    /**
     * 공지를 삽입한다. 이미 저장되어 있는 공지는 덮어쓰지 않는다.
     *
     * @param notices 삽입할 공지
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotices(notices: List<NoticeEntity>)

    /**
     * 카테고리 공지를 페이징 객체의 형태로 가져온다.
     *
     * @param categoryName 카테고리 이름
     * @return 주어진 카테고리 공지의 페이징 객체
     */
    @Query("SELECT * FROM NoticeEntity WHERE category = :categoryName ORDER BY isImportant, postedDate DESC, articleId DESC")
    fun getNotices(categoryName: String): PagingSource<Int, NoticeEntity>

    @Query("SELECT * FROM NoticeEntity ORDER BY isImportant")
    fun getOldNoticeList(): Flow<List<NoticeEntity>>

    @Query("SELECT articleId FROM NoticeEntity WHERE isRead = :value ORDER BY isImportant")
    fun getReadNoticeList(value: Boolean): Flow<List<String>>

    @Query("SELECT * FROM NoticeEntity WHERE isSaved = :isSaved ORDER BY isImportant")
    fun getNoticesBySaved(isSaved: Boolean): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM NoticeEntity WHERE isSaved = :isSaved ORDER BY isImportant")
    fun getSavedNoticeList(isSaved: Boolean): List<NoticeEntity>

    @Query("SELECT id FROM NoticeEntity WHERE articleId = :articleId AND category = :category")
    suspend fun getNoticeId(articleId: String, category: String): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNotice(notice: NoticeEntity)

    @Query("UPDATE NoticeEntity SET isRead = 1 WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeAsRead(articleId: String, category: String)

    @Query("UPDATE NoticeEntity SET isSaved = :isSaved WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeSaveState(articleId: String, category: String, isSaved: Boolean)

    @Query("UPDATE NoticeEntity SET isReadOnStorage = :isSaved WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeAsReadOnStorage(articleId: String, category: String, isSaved: Boolean)

    @Query("UPDATE NoticeEntity SET id = :id WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeId(articleId: String, category: String, id: Int)

    @Query("UPDATE NoticeEntity SET commentCount = :commentCount WHERE articleId = :articleId AND category = :category AND commentCount != :commentCount")
    suspend fun updateNoticeCommentCount(articleId: String, category: String, commentCount: Int)

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

    @Query("SELECT id FROM NoticeEntity WHERE articleId = :articleId AND department LIKE :shortName")
    suspend fun getDepartmentNoticeId(articleId: String, shortName: String): Int?

    @Query("UPDATE NoticeEntity SET id = :id WHERE articleId = :articleId AND department LIKE :shortName")
    suspend fun updateDepartmentNoticeId(articleId: String, shortName: String, id: Int)

    @Query("UPDATE NoticeEntity SET commentCount = :commentCount WHERE articleId = :articleId AND department LIKE :shortName AND commentCount != :commentCount")
    suspend fun updateDepartmentNoticeCommentCount(
        articleId: String,
        shortName: String,
        commentCount: Int
    )

    @Query("DELETE FROM NoticeEntity WHERE department LIKE :shortName")
    suspend fun clearDepartment(shortName: String)

    //not using now
    @Query("DELETE FROM NoticeEntity")
    suspend fun deleteAllNoticeRecord()
}