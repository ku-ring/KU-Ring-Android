package com.ku_stacks.ku_ring.library

import com.ku_stacks.ku_ring.domain.LibraryRoom
import com.ku_stacks.ku_ring.remote.library.response.LibraryRoomBranchResponse
import com.ku_stacks.ku_ring.remote.library.response.LibraryRoomListResponse
import com.ku_stacks.ku_ring.remote.library.response.LibraryRoomResponse
import com.ku_stacks.ku_ring.remote.library.response.LibraryRoomSeatResponse
import com.ku_stacks.ku_ring.remote.library.response.LibraryRoomTypeResponse
import com.ku_stacks.ku_ring.remote.library.response.LibrarySeatResponse

object LibraryTestUtil {
    fun mockLibrarySeatResponse() = LibrarySeatResponse(
        success = true,
        code = "success.retrieved",
        message = "조회되었습니다.",
        data = LibraryRoomListResponse(
            resultCount = 1,
            libraryRooms = listOf(
                LibraryRoomResponse(
                    id = 102,
                    roomName = "제 1 열람실 (23구역)",
                    roomType = LibraryRoomTypeResponse(
                        id = 1,
                        roomName = "열람실",
                        sortOrder = 1
                    ),
                    awaitable = true,
                    isChargeable = true,
                    branch = LibraryRoomBranchResponse(
                        id = 1,
                        roomBranchName = "상허기념도서관",
                        alias = "상허",
                        libraryCode = "211004",
                        sortOrder = 1
                    ),
                    unableMessage = null,
                    seats = LibraryRoomSeatResponse(
                        total = 219,
                        occupied = 82,
                        waiting = 0,
                        available = 137
                    )
                )
            )
        )
    )


    fun mockLibraryRoomList() = listOf(
        LibraryRoom(
            name = "제 1 열람실 (A구역)",
            totalSeats = 219,
            occupiedSeats = 82,
            availableSeats = 137
        )
    )
}