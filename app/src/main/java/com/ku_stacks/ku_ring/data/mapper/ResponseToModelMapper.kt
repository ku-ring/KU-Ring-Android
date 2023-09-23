package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.api.response.DepartmentResponse
import com.ku_stacks.ku_ring.domain.Department

fun DepartmentResponse.toDepartment() = Department(
    name = name!!,
    shortName = shortName!!,
    koreanName = this.korName!!,
    isSubscribed = false,
    isSelected = false,
    isNotificationEnabled = false
)