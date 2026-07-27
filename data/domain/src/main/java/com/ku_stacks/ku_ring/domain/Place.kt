package com.ku_stacks.ku_ring.domain

data class Place (
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val priority: Priority,
    val number: Int? = null,
    val iconUrl: String? = null,
    val phone: String? = null,
    val data: String? = null,
    val imageUrl: String? = null,
    val operationHours: PlaceOperationHours? = null,
    val facilities: List<PlaceFacility> = emptyList(),
) {
    enum class Priority {
        HIGH, MIDDLE, LOW
        ;
        companion object {
            fun from(value: String) = try {
                Priority.valueOf(value.uppercase())
            } catch (_: Exception) {
                LOW
            }
        }
    }
}

data class PlaceOperationHours(
    val current: String? = null,
    val semester: String? = null,
    val vacation: String? = null,
)

data class PlaceFacility(
    val id: Long,
    val name: String,
    val category: String,
    val categoryKor: String,
    val location: String? = null,
    val operationHours: PlaceOperationHours? = null,
    val imageUrl: String? = null,
    val quantity: Int? = null,
    val externalUrl: String? = null,
    val building: PlaceBuilding? = null,
)

data class PlaceCategory(
    val name: String,
    val korName: String,
    val displayOrder: Int,
)

data class PlaceBuilding(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)
