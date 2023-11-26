package com.ku_stacks.ku_ring.department.repository

import com.ku_stacks.ku_ring.domain.Department
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    suspend fun updateDepartmentsFromRemote()
    suspend fun insertDepartments(departments: List<Department>)
    suspend fun getAllDepartments(): List<Department>
    suspend fun getDepartmentsByKoreanName(koreanName: String): List<Department>
    suspend fun getSubscribedDepartments(): List<Department>
    suspend fun getSubscribedDepartmentsAsFlow(): Flow<List<Department>>
    suspend fun updateSubscribeStatus(name: String, isSubscribed: Boolean)
    suspend fun removeDepartments(departments: List<Department>)
    suspend fun clearDepartments()
    suspend fun fetchSubscribedDepartments(): List<Department>
    suspend fun saveSubscribedDepartmentsToRemote(departments: List<Department>)
}
