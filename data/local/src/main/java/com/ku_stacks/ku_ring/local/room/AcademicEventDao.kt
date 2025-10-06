package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ku_stacks.ku_ring.local.entity.AcademicEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademicEventDao {
    @Upsert
    suspend fun insertAcademicEvents(events: List<AcademicEventEntity>)

    @Query(
        "SELECT * FROM AcademicEventEntity " +
                "WHERE NOT (startTime > :endDate or endTime < :startDate) " +
                "ORDER BY startTime ASC, endTime ASC"
    )
    suspend fun getAcademicEvents(startDate: String, endDate: String): List<AcademicEventEntity>

    @Query(
        "SELECT * FROM AcademicEventEntity " +
                "WHERE NOT (startTime > :endDate or endTime < :startDate) " +
                "ORDER BY startTime ASC, endTime ASC"
    )
    fun getAcademicEventsAsFlow(startDate: String, endDate: String): Flow<List<AcademicEventEntity>>
}