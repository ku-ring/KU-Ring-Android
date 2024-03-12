package com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe

import com.ku_stacks.ku_ring.domain.Department

@Deprecated("학과별 공지 composable 업데이트 후 삭제")
sealed interface DepartmentStateData {
    object Loading : DepartmentStateData

    data class Error(val message: String? = null) : DepartmentStateData

    data class Success(
        val list: List<Department> = emptyList()
    ) : DepartmentStateData
}
