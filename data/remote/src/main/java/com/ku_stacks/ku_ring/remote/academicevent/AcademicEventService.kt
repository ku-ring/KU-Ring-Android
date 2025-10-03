package com.ku_stacks.ku_ring.remote.academicevent

import com.ku_stacks.ku_ring.remote.academicevent.response.AcademicEventListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AcademicEventService {
    @GET("v2/academic-events")
    suspend fun fetchAcademicEvents(
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
    ): AcademicEventListResponse
}