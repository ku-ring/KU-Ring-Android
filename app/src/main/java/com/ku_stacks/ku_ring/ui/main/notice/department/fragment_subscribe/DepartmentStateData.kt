package com.ku_stacks.ku_ring.ui.main.notice.department.fragment_subscribe

import com.ku_stacks.ku_ring.domain.Department

sealed interface DepartmentStateData {
    object Loading : DepartmentStateData

    data class Error(val message: String? = null) : DepartmentStateData

    data class Success(
        val list: List<Department> = emptyList()
    ) : DepartmentStateData
}
