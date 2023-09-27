package com.ku_stacks.ku_ring.remote.department.response

data class DepartmentListResponse(
    val code: Int,
    val message: String?,
    val data: List<DepartmentResponse>?,
)
