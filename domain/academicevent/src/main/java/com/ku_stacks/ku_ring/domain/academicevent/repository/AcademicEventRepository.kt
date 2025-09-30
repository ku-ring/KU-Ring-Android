package com.ku_stacks.ku_ring.domain.academicevent.repository

import com.ku_stacks.ku_ring.domain.AcademicEvent

interface AcademicEventRepository {
    suspend fun fetchAcademicEventsFromRemote(
        startDate: String? = null,
        endDate: String? = null,
    )

    suspend fun getAcademicEvents(
        startDate: String,
        endDate: String,
    ): List<AcademicEvent>
}