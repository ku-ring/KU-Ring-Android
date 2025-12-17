package com.ku_stacks.ku_ring.domain

data class Place (
    val id: String,
    val name: String,
    val category: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
)