package com.ku_stacks.ku_ring.place.model

import kotlinx.serialization.Serializable

@Serializable
internal data class JsonPlace(
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val inCampus: Boolean,
    val number: Int? = null,
    val iconUrl: String? = null,
    val latitude: Double,
    val longitude: Double,
    val phone: String? = null,
    val data: String? = null,
    val places: Map<String, List<String>> = emptyMap(),
    val parentId: String,
    val priority: String,
)
