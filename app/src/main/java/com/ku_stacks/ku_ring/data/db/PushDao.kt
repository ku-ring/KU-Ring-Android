package com.ku_stacks.ku_ring.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface PushDao {
    //FCM Service 쪽에서는 dispose가 애매해서 코루틴 사용
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(pushEntity: PushEntity)

    @Query("UPDATE PushEntity SET isNew = :value WHERE articleId = :articleId")
    fun updateConfirmedNotification(articleId: String, value: Boolean): Completable

    @Query("SELECT * FROM PushEntity ORDER BY postedDate DESC")
    fun getNotification(): Single<List<PushEntity>>

    //not using now
    @Query("DELETE FROM PushEntity")
    fun deleteAllNotification(): Completable
}