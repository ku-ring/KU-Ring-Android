package com.ku_stacks.ku_ring.remote.library

import com.ku_stacks.ku_ring.remote.library.response.LibrarySeatResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LibraryService {
    @GET("1/seat-rooms")
    suspend fun fetchLibrarySeatStatus(
        @Query("smufMethodCode") methodCode: String,
        @Query("roomTypeId") roomTypeId: Int,
        @Query("branchGroupId") branchGroupId: Int,
    ): LibrarySeatResponse
}