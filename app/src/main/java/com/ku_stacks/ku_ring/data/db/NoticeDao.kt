package com.ku_stacks.ku_ring.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface NoticeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotice(notice: NoticeEntity): Completable

    @Query("SELECT * FROM NoticeEntity")
    fun getNoticeList(): Flowable<List<NoticeEntity>>
}