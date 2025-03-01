package com.ku_stacks.ku_ring.local.room

import androidx.annotation.VisibleForTesting
import androidx.room.*
import com.ku_stacks.ku_ring.local.entity.CategoryOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryOrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryOrder(order: CategoryOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryOrder(orders: List<CategoryOrderEntity>)

    @Query("SELECT * FROM CategoryOrderEntity ORDER BY categoryOrder ASC")
    suspend fun getCategoryOrders(): List<CategoryOrderEntity>

    @Query("SELECT * FROM CategoryOrderEntity ORDER BY categoryOrder ASC")
    fun getCategoryOrdersAsFlow(): Flow<List<CategoryOrderEntity>>

    @Update
    suspend fun updateCategoryOrder(order: CategoryOrderEntity)

    @Update
    suspend fun updateCategoryOrders(order: List<CategoryOrderEntity>)

    @VisibleForTesting
    @Query("DELETE FROM CategoryOrderEntity")
    suspend fun clear()
}