package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNotice(notice: NoticeEntity): Completable

    @Query("SELECT * FROM NoticeEntity")
    fun getNoticeRecord(): Single<List<NoticeEntity>>

    @Query("SELECT * FROM NoticeEntity WHERE isRead = :value")
    fun getReadNoticeRecord(value: Boolean): Flowable<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateNotice(notice: NoticeEntity): Completable

    @Query("DELETE FROM NoticeEntity")
    fun deleteAllNoticeRecord(): Completable
}