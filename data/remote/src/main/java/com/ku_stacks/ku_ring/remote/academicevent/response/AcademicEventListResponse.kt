package com.ku_stacks.ku_ring.remote.academicevent.response

import kotlinx.serialization.Serializable

@Serializable
data class AcademicEventListResponse (
    val code: Int,
    val message: String,
    val data: List<AcademicEventResponse>,
)
