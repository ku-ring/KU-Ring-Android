package com.ku_stacks.ku_ring.remote.department

import com.ku_stacks.ku_ring.remote.department.request.DepartmentSubscribeRequest
import com.ku_stacks.ku_ring.remote.department.response.DepartmentListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DepartmentService {
    @GET("v2/notices/departments")
    suspend fun fetchDepartmentList(): DepartmentListResponse

    @POST("v2/users/subscriptions/departments")
    suspend fun subscribeDepartments(
        @Header("User-Token") fcmToken: String,
        @Body body: DepartmentSubscribeRequest,
    ): DepartmentListResponse

    @GET("v2/users/subscriptions/departments")
    suspend fun getSubscribedDepartments(@Header("User-Token") fcmToken: String): DepartmentListResponse
}