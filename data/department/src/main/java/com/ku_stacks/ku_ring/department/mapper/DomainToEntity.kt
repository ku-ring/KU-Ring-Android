package com.ku_stacks.ku_ring.department.mapper

import com.ku_stacks.ku_ring.domain.Department
import com.ku_stacks.ku_ring.local.entity.DepartmentEntity

fun List<Department>.toEntityList(): List<DepartmentEntity> = map { it.toEntity() }

fun Department.toEntity(): DepartmentEntity = DepartmentEntity(
    name = name,
    shortName = shortName,
    koreanName = koreanName,
    isSubscribed = isSubscribed,
    isMainDepartment = isSelected,
)