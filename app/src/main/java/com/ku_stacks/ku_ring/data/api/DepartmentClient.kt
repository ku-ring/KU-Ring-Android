package com.ku_stacks.ku_ring.data.api

import javax.inject.Inject

class DepartmentClient @Inject constructor(private val departmentService: DepartmentService) {
    suspend fun fetchDepartmentList() = departmentService.fetchDepartmentList()
}