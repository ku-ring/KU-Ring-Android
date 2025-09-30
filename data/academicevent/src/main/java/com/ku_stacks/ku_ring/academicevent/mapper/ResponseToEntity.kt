package com.ku_stacks.ku_ring.academicevent.mapper

import com.ku_stacks.ku_ring.local.entity.AcademicEventEntity
import com.ku_stacks.ku_ring.remote.academicevent.response.AcademicEventResponse

internal fun List<AcademicEventResponse>.toEntity() = map { it.toEntity() }

internal fun AcademicEventResponse.toEntity() = AcademicEventEntity(
    id = id,
    eventUid = eventUid,
    summary = summary,
    description = description,
    category = category,
    startTime = startTime,
    endTime = endTime,
)