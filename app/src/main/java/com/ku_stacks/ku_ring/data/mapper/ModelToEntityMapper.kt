package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.db.DepartmentEntity
import com.ku_stacks.ku_ring.domain.Department

fun List<Department>.toEntityList(): List<DepartmentEntity> = map { it.toEntity() }

fun Department.toEntity(): DepartmentEntity = DepartmentEntity(
    name = name,
    shortName = shortName,
    koreanName = koreanName,
    isSubscribed = isSubscribed,
)