package com.ku_stacks.ku_ring.domain

import java.time.LocalDateTime

data class KuringBotMessage(
    val id: Int,
    val message: String,
    val postedDate: LocalDateTime,
    val isQuery: Boolean,
)