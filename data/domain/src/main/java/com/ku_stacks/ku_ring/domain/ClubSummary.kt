package com.ku_stacks.ku_ring.domain

import kotlinx.datetime.LocalDateTime

data class ClubSummary (
    val id: Int,
    val name: String,
    val summary: String,
    val category: ClubCategory,
    val division: ClubDivision,
    val posterImageUrl: String?,
    val isSubscribed: Boolean,
    val subscribeCount: Int,
    val recruitmentStart: LocalDateTime?,
    val recruitmentEnd: LocalDateTime?,
)
