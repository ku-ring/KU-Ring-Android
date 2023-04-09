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
    // not null: 최신 데이터가 캐시됨
    // null: 데이터가 업데이트되어 새 데이터를 가져와야 함
    private var departments: List<Department>? = null

    override suspend fun insertDepartment(department: Department) {
        withContext(ioDispatcher) {
            departmentDao.insertDepartment(department.toEntity())
        }
        departments = null
    }

    override suspend fun insertDepartments(departments: List<Department>) {
        withContext(ioDispatcher) {
            departmentDao.insertDepartments(departments.toEntityList())
        }
        this.departments = null
    }

    override suspend fun getAllDepartments(): List<Department> {
        return departments ?: withContext(ioDispatcher) {
            departmentDao.getAllDepartments().toDepartmentList().also {
                departments = it
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
        return withContext(ioDispatcher) {
            departmentDao.getDepartmentsBySubscribedAsFlow(true).map {
                it.toDepartmentList()
            }
        }
    }

    override suspend fun updateSubscribeStatus(name: String, isSubscribed: Boolean) {
        withContext(ioDispatcher) {
            departmentDao.updateSubscribeStatus(name, isSubscribed)
        }
        departments = null
    }

    override suspend fun removeDepartments(departments: List<Department>) {
        withContext(ioDispatcher) {
            departmentDao.removeDepartments(departments.toEntityList())
        }
        this.departments = null
    }

    override suspend fun clearDepartments() {
        withContext(ioDispatcher) {
            departmentDao.clearDepartments()
        }
        departments = null
    }
}