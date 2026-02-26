package com.ku_stacks.ku_ring.remote.club

import com.ku_stacks.ku_ring.remote.club.request.ClubSubscribeRequest
import com.ku_stacks.ku_ring.remote.club.request.ClubUnsubscribeRequest
import com.ku_stacks.ku_ring.remote.club.response.ClubDetailResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubListResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubSubscribeResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubUnsubscribeResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClubService {
    @POST("v2/users/bookmarks/clubs")
    suspend fun subscribeClub(
        @Body request: ClubSubscribeRequest,
    ): DefaultResponse<ClubSubscribeResponse>

    @DELETE("v2/users/bookmarks/clubs")
    suspend fun unsubscribeClub(
        @Body request: ClubUnsubscribeRequest,
    ): DefaultResponse<ClubUnsubscribeResponse>

    @GET("v2/clubs/{id}")
    suspend fun getClubDetail(
        @Path("id") clubId: Int,
    ): DefaultResponse<ClubDetailResponse>

    @GET("v2/clubs")
    suspend fun getClubs(
        @Query("category") category: String,
        @Query("division") division: String,
    ): DefaultResponse<ClubListResponse>

    @GET("v2/users/bookmarks/clubs")
    suspend fun getSubscribedClubs(): DefaultResponse<ClubListResponse>
}
