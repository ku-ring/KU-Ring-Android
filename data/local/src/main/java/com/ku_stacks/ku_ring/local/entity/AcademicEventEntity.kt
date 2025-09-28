package com.ku_stacks.ku_ring.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AcademicEventEntity(
    @ColumnInfo("id") val id: Long,
    @PrimaryKey @ColumnInfo("eventUid") val eventUid: String,
    @ColumnInfo("summary") val summary: String,
    @ColumnInfo("description") val description: String?,
    @ColumnInfo("category") val category: String,
    @ColumnInfo("startTime") val startTime: String,
    @ColumnInfo("endTime") val endTime: String,
)