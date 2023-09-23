package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.DepartmentEntity
import com.ku_stacks.ku_ring.domain.Department


fun List<DepartmentEntity>.toDepartmentList() = map { it.toDepartment() }

fun DepartmentEntity.toDepartment() = Department(
    name = name,
    shortName = shortName,
    koreanName = koreanName,
    isSubscribed = isSubscribed,
    isSelected = false,
    isNotificationEnabled = false,
)