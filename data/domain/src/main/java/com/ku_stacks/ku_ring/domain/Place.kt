package com.ku_stacks.ku_ring.domain

data class Place (
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val priority: Priority,
) {
    enum class Priority {
        HIGH, MIDDLE, LOW
        ;
        companion object {
            fun from(value: String) = try {
                Priority.valueOf(value.uppercase())
            } catch (e: Exception) {
                LOW
            }
        }
    }
}