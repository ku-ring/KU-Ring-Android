package com.ku_stacks.ku_ring.domain

data class AcademicEvent (
    val id: Long,
    val summary: String,
    val category: String,
    val startTime: String,
    val endTime: String,
)