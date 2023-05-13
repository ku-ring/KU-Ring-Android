package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.model.Department
import kotlinx.coroutines.flow.Flow

interface DepartmentRepository {
    suspend fun insertAllDepartmentsFromRemote()
    suspend fun fetchAllDepartmentsFromRemote(): List<Department>?
    suspend fun insertDepartment(department: Department)
    suspend fun insertDepartments(departments: List<Department>)
    suspend fun getAllDepartments(): List<Department>
    suspend fun getDepartmentsByKoreanName(koreanName: String): List<Department>
    suspend fun getSubscribedDepartments(): List<Department>
    suspend fun getSubscribedDepartmentsAsFlow(): Flow<List<Department>>
    suspend fun updateSubscribeStatus(name: String, isSubscribed: Boolean)
    suspend fun removeDepartments(departments: List<Department>)
    suspend fun clearDepartments()
}
