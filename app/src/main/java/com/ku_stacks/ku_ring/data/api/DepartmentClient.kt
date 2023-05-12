package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.request.DepartmentSubscribeRequest
import javax.inject.Inject

class DepartmentClient @Inject constructor(private val departmentService: DepartmentService) {
    suspend fun fetchDepartmentList() = departmentService.fetchDepartmentList()

    suspend fun subscribeDepartments(fcmToken: String, departments: List<String>) =
        departmentService.subscribeDepartments(fcmToken, DepartmentSubscribeRequest(departments))

    suspend fun getSubscribedDepartments(fcmToken: String) =
        departmentService.getSubscribedDepartments(fcmToken)
}