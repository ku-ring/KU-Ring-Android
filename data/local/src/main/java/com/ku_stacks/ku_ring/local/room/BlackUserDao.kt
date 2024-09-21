package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.BlackUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlackUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun blockUser(user: BlackUserEntity)

    @Query("SELECT * FROM BlackUserEntity")
    fun getBlackList(): Flow<List<BlackUserEntity>>
}