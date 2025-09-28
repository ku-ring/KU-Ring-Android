package com.ku_stacks.ku_ring.remote.academicevent

import com.ku_stacks.ku_ring.remote.academicevent.response.AcademicEventListResponse
import javax.inject.Inject

class AcademicEventClient @Inject constructor(
    private val academicEventService: AcademicEventService
) {
    suspend fun fetchAcademicEvents(
        startDate: String,
        endDate: String
    ): AcademicEventListResponse = academicEventService.fetchAcademicEvents(startDate, endDate)
}