package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNoticeAsOld(notice: NoticeEntity): Completable

    @Query("SELECT * FROM NoticeEntity")
    fun getOldNoticeList(): Single<List<NoticeEntity>>

    @Query("SELECT articleId FROM NoticeEntity WHERE isRead = :value")
    fun getReadNoticeList(value: Boolean): Flowable<List<String>>

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