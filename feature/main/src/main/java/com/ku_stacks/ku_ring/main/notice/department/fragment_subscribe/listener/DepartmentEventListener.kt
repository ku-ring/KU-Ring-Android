package com.ku_stacks.ku_ring.main.notice.department.fragment_subscribe.listener

import com.ku_stacks.ku_ring.domain.Department

interface DepartmentEventListener {
    fun onClickDepartment(department: Department)
}
