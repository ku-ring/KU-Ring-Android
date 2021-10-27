package com.ku_stacks.ku_ring.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface PushDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(pushEntity: PushEntity): Completable

    @Query("SELECT * FROM PushEntity")
    fun getNotification(): Flowable<List<PushEntity>>
}