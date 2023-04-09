package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.DepartmentListResponse
import retrofit2.http.GET

interface DepartmentService {
    @GET("v2/notices/departments")
    suspend fun fetchDepartmentList(): DepartmentListResponse
}