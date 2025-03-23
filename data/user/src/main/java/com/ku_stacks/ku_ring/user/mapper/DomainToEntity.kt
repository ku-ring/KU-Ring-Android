package com.ku_stacks.ku_ring.user.mapper

import com.ku_stacks.ku_ring.domain.CategoryOrder
import com.ku_stacks.ku_ring.local.entity.CategoryOrderEntity

fun CategoryOrder.toEntity() = CategoryOrderEntity(
    koreanName = koreanName,
    shortName = shortName,
    order = order,
)

fun List<CategoryOrder>.toEntity() = map { it.toEntity() }