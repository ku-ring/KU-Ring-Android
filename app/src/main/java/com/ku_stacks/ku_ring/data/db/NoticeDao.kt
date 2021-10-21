package com.ku_stacks.ku_ring.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotice(notice: NoticeEntity): Completable

    @Query("SELECT * FROM NoticeEntity")
    fun getNoticeRecord(): Single<List<NoticeEntity>>

//    @Query("SELECT EXISTS (SELECT * FROM NoticeEntity WHERE articleId = :articleId )")
//    fun hasNotice(articleId: String): Completable
}