package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface PushDao {
    //FCM Service 쪽에서는 dispose가 애매해서 코루틴 사용
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(pushEntity: PushEntity)

    @Query("UPDATE PushEntity SET isNew = :value WHERE articleId = :articleId and isNew = not :value")
    fun updateToReadNotification(articleId: String, value: Boolean): Completable

    @Query("SELECT * FROM PushEntity ORDER BY postedDate DESC, receivedDate DESC")
    fun getNotification(): Flowable<List<PushEntity>>

    @Query("SELECT COUNT(articleId) FROM PushEntity WHERE isNew = :value")
    fun getNotificationCount(value: Boolean): Flowable<Int>

    @Query("DELETE FROM PushEntity WHERE articleId = :articleId")
    fun deleteNotification(articleId: String): Completable

    //not using now
    @Query("DELETE FROM PushEntity")
    fun deleteAllNotification(): Completable
}