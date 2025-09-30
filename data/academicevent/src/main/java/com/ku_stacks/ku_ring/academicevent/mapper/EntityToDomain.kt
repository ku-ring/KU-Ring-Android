package com.ku_stacks.ku_ring.academicevent.mapper

import com.ku_stacks.ku_ring.domain.AcademicEvent
import com.ku_stacks.ku_ring.local.entity.AcademicEventEntity
import kotlinx.datetime.LocalDateTime

internal fun AcademicEventEntity.toDomain() = AcademicEvent(
    id = id,
    summary = summary,
    description = description,
    category = category,
    startDateTime = LocalDateTime.parse(startTime),
    endDateTime = LocalDateTime.parse(endTime),
)