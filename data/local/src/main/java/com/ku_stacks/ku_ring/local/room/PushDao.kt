package com.ku_stacks.ku_ring.local.room

import androidx.paging.PagingSource
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

    @Query("UPDATE PushEntity SET isNew = :value WHERE id = :id and isNew = not :value")
    suspend fun updateNotificationAsOld(id: Int, value: Boolean)

    @Query("SELECT * FROM PushEntity ORDER BY receivedDate DESC")
    fun getNotificationList(): PagingSource<Int, PushEntity>

    @Query("SELECT COUNT(id) FROM PushEntity WHERE isNew = :value")
    fun getNotificationCount(value: Boolean): Flow<Int>

    @Query("DELETE FROM PushEntity WHERE id = :id")
    suspend fun deleteNotification(id: Int)

    //not using now
    @Query("DELETE FROM PushEntity")
    suspend fun deleteAllNotification()
}
