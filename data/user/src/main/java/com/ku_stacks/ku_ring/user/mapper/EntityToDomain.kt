package com.ku_stacks.ku_ring.user.mapper

import com.ku_stacks.ku_ring.domain.CategoryOrder
import com.ku_stacks.ku_ring.local.entity.CategoryOrderEntity

fun CategoryOrderEntity.toDomain() = CategoryOrder(
    koreanName = koreanName,
    shortName = shortName,
    order = order,
)

fun List<CategoryOrderEntity>.toDomain() = map { it.toDomain() }