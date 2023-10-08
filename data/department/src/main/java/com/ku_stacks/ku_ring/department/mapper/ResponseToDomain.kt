package com.ku_stacks.ku_ring.department.mapper

import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.remote.department.response.DepartmentResponse

fun DepartmentResponse.toDepartment() = Department(
    name = name!!,
    shortName = shortName!!,
    koreanName = this.korName!!,
    isSubscribed = false,
    isSelected = false,
    isNotificationEnabled = false
)