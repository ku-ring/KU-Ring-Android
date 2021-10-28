package com.ku_stacks.ku_ring.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface PushDao {
    //FCM Service 쪽에서는 dispose가 애매해서 코루틴 사용
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(pushEntity: PushEntity)

    @Query("UPDATE PushEntity SET isNew = :value WHERE articleId = :articleId")
    fun updateConfirmedNotification(articleId: String, value: Boolean): Completable

    @Query("SELECT * FROM PushEntity ORDER BY postedDate DESC")
    fun getNotification(): Flowable<List<PushEntity>>

    @Query("SELECT COUNT(articleId) FROM PushEntity WHERE isNew = :value")
    fun getNotificationCount(value: Boolean): Flowable<Int>

    //not using now
    @Query("DELETE FROM PushEntity")
    fun deleteAllNotification(): Completable
}