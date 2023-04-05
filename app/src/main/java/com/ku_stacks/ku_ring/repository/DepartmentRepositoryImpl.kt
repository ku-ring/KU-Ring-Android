package com.ku_stacks.ku_ring.repository

import com.ku_stacks.ku_ring.data.db.DepartmentDao
import com.ku_stacks.ku_ring.data.mapper.toDepartmentList
import com.ku_stacks.ku_ring.data.mapper.toEntity
import com.ku_stacks.ku_ring.data.mapper.toEntityList
import com.ku_stacks.ku_ring.data.model.Department
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DepartmentRepositoryImpl @Inject constructor(
    private val departmentDao: DepartmentDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : DepartmentRepository {
    private lateinit var departments: List<Department>
    private var isUpdated: Boolean = false

    override suspend fun insertDepartment(department: Department) {
        withIOContext {
            departmentDao.insertDepartment(department.toEntity())
        }
        isUpdated = true
    }

    override suspend fun insertDepartments(departments: List<Department>) {
        withIOContext {
            departmentDao.insertDepartments(departments.toEntityList())
        }
        isUpdated = true
    }

    override suspend fun getAllDepartments(): List<Department> {
        return if (::departments.isInitialized && !isUpdated) {
            departments
        } else {
            withIOContext {
                departmentDao.getAllDepartments().toDepartmentList().also {
                    departments = it
                }
            }
        }
    }

    override suspend fun getDepartmentsByKoreanName(koreanName: String): List<Department> {
        val latestDepartments = getAllDepartments()
        return latestDepartments.filter {
            it.koreanName.contains(koreanName)
        }
    }

    override suspend fun getSubscribedDepartments(): List<Department> {
        return getAllDepartments().filter { it.isSubscribed }
    }

    override suspend fun getSubscribedDepartmentsAsFlow(): Flow<List<Department>> {
        return withIOContext {
            departmentDao.getDepartmentsBySubscribedAsFlow(true).map {
                it.toDepartmentList()
            }
        }
    }

    override suspend fun updateSubscribeStatus(name: String, isSubscribed: Boolean) {
        withIOContext {
            departmentDao.updateSubscribeStatus(name, isSubscribed)
        }
        isUpdated = true
    }

    override suspend fun removeDepartments(departments: List<Department>) {
        withIOContext {
            departmentDao.removeDepartments(departments.toEntityList())
        }
        isUpdated = true
    }

    override suspend fun clearDepartments() {
        withIOContext {
            departmentDao.clearDepartments()
        }
        isUpdated = true
    }

    private suspend fun <T> withIOContext(block: suspend () -> T): T {
        return withContext(ioDispatcher) {
            block()
        }
    }
}