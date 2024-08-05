package com.ku_stacks.ku_ring.ai.mapper

import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.local.entity.KuringBotMessageEntity
import com.ku_stacks.ku_ring.util.toEpochSecond

internal fun KuringBotMessage.toEntity() = KuringBotMessageEntity(
    id = id,
    message = message,
    postedEpochSeconds = postedDate.toEpochSecond(),
    isQuery = isQuery,
)

internal fun List<KuringBotMessage>.toEntity() = map { it.toEntity() }