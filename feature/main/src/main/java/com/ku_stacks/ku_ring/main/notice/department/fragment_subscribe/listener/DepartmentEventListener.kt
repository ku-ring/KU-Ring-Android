package com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe.listener

import com.ku_stacks.ku_ring.domain.Department

@Deprecated("학과별 공지 composable 업데이트 후 삭제")
interface DepartmentEventListener {
    fun onClickDepartment(department: Department)
}
