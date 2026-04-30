package com.ku_stacks.ku_ring.remote.academicevent

import com.ku_stacks.ku_ring.remote.academicevent.response.AcademicEventResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class AcademicEventClient @Inject constructor(
    private val academicEventService: AcademicEventService
) {
    suspend fun fetchAcademicEvents(
        startDate: String?,
        endDate: String?,
    ): DefaultResponse<List<AcademicEventResponse>> =
        academicEventService.fetchAcademicEvents(startDate, endDate)
}