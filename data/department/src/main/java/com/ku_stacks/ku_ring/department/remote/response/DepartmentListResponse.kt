package com.ku_stacks.ku_ring.department.remote.response

data class DepartmentListResponse(
    val code: Int,
    val message: String?,
    val data: List<DepartmentResponse>?,
)
