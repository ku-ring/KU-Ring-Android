package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNoticeAsOld(notice: NoticeEntity): Completable

    @Query("SELECT * FROM NoticeEntity")
    fun getOldNoticeList(): Single<List<NoticeEntity>>

    @Query("SELECT articleId FROM NoticeEntity WHERE isRead = :value")
    fun getReadNoticeList(value: Boolean): Flowable<List<String>>

    @Query("SELECT * FROM NoticeEntity WHERE isSaved = :isSaved")
    fun getNoticesBySaved(isSaved: Boolean): Flow<List<NoticeEntity>>

    @Query("SELECT * FROM NoticeEntity WHERE isSaved = :isSaved")
    fun getSavedNoticeList(isSaved: Boolean): List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateNotice(notice: NoticeEntity): Completable

    @Query("UPDATE NoticeEntity SET isRead = 1 WHERE articleId = :articleId AND category = :category")
    fun updateNoticeAsRead(articleId: String, category: String): Completable

    @Query("UPDATE NoticeEntity SET isSaved = :isSaved WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeSaveState(articleId: String, category: String, isSaved: Boolean)

    @Query("UPDATE NoticeEntity SET isReadOnStorage = :isSaved WHERE articleId = :articleId AND category = :category")
    suspend fun updateNoticeAsReadOnStorage(articleId: String, category: String, isSaved: Boolean)

    @Query("UPDATE NoticeEntity SET isSaved = 0")
    suspend fun clearSavedNotices()

    @Query("SELECT COUNT(*) FROM NoticeEntity WHERE isRead = :value and articleId = :id")
    fun getCountOfReadNotice(value: Boolean, id: String): Single<Int>

    fun isReadNotice(id: String): Boolean {
        return getCountOfReadNotice(true, id).subscribeOn(Schedulers.io()).blockingGet() > 0
    }

    //not using now
    @Query("DELETE FROM NoticeEntity")
    fun deleteAllNoticeRecord(): Completable
}