package com.ku_stacks.ku_ring.library.mapper

import com.ku_stacks.ku_ring.domain.LibraryRoom
import com.ku_stacks.ku_ring.remote.library.response.LibrarySeatResponse

fun LibrarySeatResponse.toLibraryAreaList(): List<LibraryRoom> {
    return data.libraryRooms.map {
        LibraryRoom(
            name = it.roomName,
            totalSeats = it.seats.total,
            availableSeats = it.seats.available,
            occupiedSeats = it.seats.occupied
        )
    }
}