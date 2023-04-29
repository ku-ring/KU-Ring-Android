package com.ku_stacks.ku_ring.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PageKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageKeys(pageKeyEntities: List<PageKeyEntity>)

    @Query("SELECT * FROM pageKeys WHERE articleId = :articleId")
    suspend fun getPageKey(articleId: String): PageKeyEntity?
}