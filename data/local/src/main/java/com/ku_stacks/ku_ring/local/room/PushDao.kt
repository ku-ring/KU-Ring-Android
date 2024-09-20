package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.PushEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PushDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(pushEntity: PushEntity)

    @Query("UPDATE PushEntity SET isNew = :value WHERE articleId = :articleId and isNew = not :value")
    suspend fun updateNotificationAsOld(articleId: String, value: Boolean)

    @Query("SELECT * FROM PushEntity ORDER BY postedDate DESC, receivedDate DESC")
    fun getNotificationList(): Flow<List<PushEntity>>

    @Query("SELECT COUNT(articleId) FROM PushEntity WHERE isNew = :value")
    fun getNotificationCount(value: Boolean): Flow<Int>

    @Query("DELETE FROM PushEntity WHERE articleId = :articleId")
    suspend fun deleteNotification(articleId: String)

    //not using now
    @Query("DELETE FROM PushEntity")
    suspend fun deleteAllNotification()
}