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
    val imageUrl: String? = null,
    val operationHours: JsonPlaceOperationHours? = null,
    val facilities: List<JsonPlaceFacility>? = null,
    val places: Map<String, List<String>> = emptyMap(),
    val parentId: String,
    val priority: String,
)

@Serializable
internal data class JsonPlaceOperationHours(
    val current: String? = null,
    val semester: String? = null,
    val vacation: String? = null,
)

@Serializable
internal data class JsonPlaceFacility(
    val name: String,
    val category: String,
    val location: String? = null,
    val operationHours: JsonPlaceOperationHours? = null,
)
