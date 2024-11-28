package com.ku_stacks.ku_ring.domain

data class LibraryRoom(
    val name: String,
    val totalSeats: Int,
    val occupiedSeats: Int,
    val availableSeats: Int,
)
