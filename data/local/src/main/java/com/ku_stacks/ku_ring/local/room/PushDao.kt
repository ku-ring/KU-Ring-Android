package com.ku_stacks.ku_ring.local.room

import androidx.room.*
import com.ku_stacks.ku_ring.local.entity.PushEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface PushDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(pushEntity: PushEntity)

    @Query("UPDATE PushEntity SET isNew = :value WHERE articleId = :articleId and isNew = not :value")
    fun updateNotificationAsOld(articleId: String, value: Boolean): Completable

    @Query("SELECT * FROM PushEntity ORDER BY postedDate DESC, receivedDate DESC")
    fun getNotificationList(): Flowable<List<PushEntity>>

    @Query("SELECT COUNT(articleId) FROM PushEntity WHERE isNew = :value")
    fun getNotificationCount(value: Boolean): Flowable<Int>

    @Query("DELETE FROM PushEntity WHERE articleId = :articleId")
    fun deleteNotification(articleId: String): Completable

    //not using now
    @Query("DELETE FROM PushEntity")
    fun deleteAllNotification(): Completable
}