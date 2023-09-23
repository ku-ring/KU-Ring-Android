package com.ku_stacks.ku_ring.user.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface BlackUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun blockUser(user: BlackUserEntity): Completable

    @Query("SELECT * FROM BlackUserEntity")
    fun getBlackList(): Single<List<BlackUserEntity>>
}