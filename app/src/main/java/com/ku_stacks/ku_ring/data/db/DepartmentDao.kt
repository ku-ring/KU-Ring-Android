package com.ku_stacks.ku_ring.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepartmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepartment(department: DepartmentEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDepartments(departments: List<DepartmentEntity>)

    @Query("SELECT * FROM departments")
    suspend fun getAllDepartments(): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE name = :name")
    suspend fun getDepartmentsByName(name: String): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE koreanName LIKE '%' || :koreanName || '%'")
    suspend fun getDepartmentsByKoreanName(koreanName: String): List<DepartmentEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM departments LIMIT 1)")
    suspend fun isNotEmpty(): Boolean

    suspend fun isEmpty(): Boolean = !isNotEmpty()

    @Query("UPDATE departments SET shortName = :shortName, koreanName = :koreanName WHERE name = :name")
    suspend fun updateDepartment(name: String, shortName: String, koreanName: String)

    @Query("SELECT * FROM departments WHERE isSubscribed = :isSubscribed")
    suspend fun getDepartmentsBySubscribed(isSubscribed: Boolean): List<DepartmentEntity>

    @Query("SELECT * FROM departments WHERE isSubscribed = :isSubscribed")
    fun getDepartmentsBySubscribedAsFlow(isSubscribed: Boolean): Flow<List<DepartmentEntity>>

    @Query("UPDATE departments SET isSubscribed = :isSubscribed WHERE name = :name")
    fun updateSubscribeStatus(name: String, isSubscribed: Boolean)

    @Delete
    suspend fun removeDepartments(departments: List<DepartmentEntity>)

    @Query("DELETE from departments")
    suspend fun clearDepartments()
}