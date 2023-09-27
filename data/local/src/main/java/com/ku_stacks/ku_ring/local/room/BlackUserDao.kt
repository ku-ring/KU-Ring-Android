package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.BlackUserEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface BlackUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun blockUser(user: BlackUserEntity): Completable

    @Query("SELECT * FROM BlackUserEntity")
    fun getBlackList(): Single<List<BlackUserEntity>>
}