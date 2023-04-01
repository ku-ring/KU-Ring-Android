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
    override suspend fun insertDepartment(department: Department) {
        withIOContext {
            departmentDao.insertDepartment(department.toEntity())
        }
    }

    override suspend fun insertDepartments(departments: List<Department>) {
        withIOContext {
            departmentDao.insertDepartments(departments.toEntityList())
        }
    }

    override suspend fun getAllDepartments(): List<Department> {
        return withIOContext {
            departmentDao.getAllDepartments().toDepartmentList()
        }
    }

    override suspend fun getDepartmentsByKoreanName(koreanName: String): List<Department> {
        // TODO: Repository에서 List<Department>를 cache한 후, 이 데이터에서 검색하기
        return withIOContext {
            departmentDao.getDepartmentsByKoreanName(koreanName).toDepartmentList()
        }
    }

    override suspend fun getSubscribedDepartments(): List<Department> {
        return withIOContext {
            departmentDao.getDepartmentsBySubscribed(true).toDepartmentList()
        }
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
    }

    override suspend fun removeDepartments(departments: List<Department>) {
        withIOContext {
            departmentDao.removeDepartments(departments.toEntityList())
        }
    }

    override suspend fun clearDepartments() {
        withIOContext {
            departmentDao.clearDepartments()
        }
    }

    private suspend fun <T> withIOContext(block: suspend () -> T): T {
        return withContext(ioDispatcher) {
            block()
        }
    }
}