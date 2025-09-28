package com.ku_stacks.ku_ring.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ku_stacks.ku_ring.local.entity.AcademicEventEntity

@Dao
interface AcademicEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAcademicEvents(events: List<AcademicEventEntity>)

    @Query("SELECT * FROM AcademicEventEntity WHERE startTime >= :startDate AND endTime <= :endDate")
    suspend fun getAcademicEvents(startDate: String, endDate: String): List<AcademicEventEntity>
}