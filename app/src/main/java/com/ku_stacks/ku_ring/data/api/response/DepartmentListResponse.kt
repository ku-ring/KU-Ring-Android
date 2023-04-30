package com.ku_stacks.ku_ring.data.api.response

data class DepartmentListResponse(
    val code: Int,
    val message: String?,
    val data: List<DepartmentResponse>?,
)
