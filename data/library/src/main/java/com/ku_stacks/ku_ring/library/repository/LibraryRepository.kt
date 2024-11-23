package com.ku_stacks.ku_ring.library.repository

import com.ku_stacks.ku_ring.domain.LibraryRoom

interface LibraryRepository {
    suspend fun getRemainingSeats(): Result<List<LibraryRoom>>
}