package com.ku_stacks.ku_ring.ai.mapper

import com.ku_stacks.ku_ring.ai.systemDefaultZone
import com.ku_stacks.ku_ring.domain.KuringBotMessage
import com.ku_stacks.ku_ring.domain.MessageType
import com.ku_stacks.ku_ring.local.entity.KuringBotMessageEntity
import java.time.LocalDateTime

internal fun KuringBotMessageEntity.toDomain() = KuringBotMessage(
    id = id,
    message = message,
    postedDate = LocalDateTime.ofEpochSecond(postedEpochSeconds, 0, systemDefaultZone()),
    type = MessageType.from(type),
)

internal fun List<KuringBotMessageEntity>.toDomain() = map { it.toDomain() }