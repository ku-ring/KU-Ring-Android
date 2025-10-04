package com.ku_stacks.ku_ring.domain.academicevent.repository

import com.ku_stacks.ku_ring.domain.AcademicEvent
import kotlinx.coroutines.flow.Flow

interface AcademicEventRepository {
    /**
     * fetches academic events that have a date range which intersects with the given range from the remote and saves in the local database
     * @param startDate start date of the academic events (format: yyyy-MM-dd, no limit if null)
     * @param endDate end date of the academic events (format: yyyy-MM-dd, no limit if null)
     */
    suspend fun fetchAcademicEventsFromRemote(
        startDate: String? = null,
        endDate: String? = null,
    ): Result<Unit>

    /**
     * fetches academic events from the local database that have a date range which intersects with the given range
     * @param startDate start date of the academic events (format: yyyy-MM-dd, no limit if null)
     * @param endDate end date of the academic events (format: yyyy-MM-dd, no limit if null)
     */
    suspend fun getAcademicEvents(
        startDate: String,
        endDate: String,
    ): Result<List<AcademicEvent>>

    /**
     * gets flow of academic events from the local database
     * that have a date range which intersects with the given range
     * @param startDate start date of the academic events (format: yyyy-MM-dd, no limit if null)
     * @param endDate end date of the academic events (format: yyyy-MM-dd, no limit if null)
     */
    fun getAcademicEventsAsFlow(
        startDate: String,
        endDate: String,
    ): Flow<List<AcademicEvent>>
}