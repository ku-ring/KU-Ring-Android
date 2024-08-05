package com.ku_stacks.ku_ring.ai.mapper

import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.local.entity.KuringBotMessageEntity
import com.ku_stacks.ku_ring.util.toLocalDateTime

internal fun KuringBotMessageEntity.toDomain() = KuringBotMessage(
    id = id,
    message = message,
    postedDate = postedEpochSeconds.toLocalDateTime(),
    isQuery = isQuery,
)

internal fun List<KuringBotMessageEntity>.toDomain() = map { it.toDomain() }