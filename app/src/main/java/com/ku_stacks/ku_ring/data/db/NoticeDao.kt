package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

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

    @Query("SELECT COUNT(*) FROM NoticeEntity WHERE isRead = :value and articleId = :id")
    fun getCountForReadNotice(value: Boolean, id: String): Single<Int>

    fun isReadNotice(id: String): Boolean {
        return getCountForReadNotice(true, id).subscribeOn(Schedulers.io()).blockingGet() > 0
    }

    //not using now
    @Query("DELETE FROM NoticeEntity")
    fun deleteAllNoticeRecord(): Completable
}