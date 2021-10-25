package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotice(notice: NoticeEntity): Completable

    @Query("SELECT * FROM NoticeEntity")
    fun getNoticeRecord(): Single<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateNotice(notice: NoticeEntity): Completable

    @Query("DELETE FROM NoticeEntity")
    fun deleteAllNoticeRecord(): Completable

//    @Query("SELECT EXISTS (SELECT * FROM NoticeEntity WHERE articleId = :articleId )")
//    fun hasNotice(articleId: String): Completable
}