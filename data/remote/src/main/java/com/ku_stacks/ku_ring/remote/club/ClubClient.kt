package com.ku_stacks.ku_ring.remote.club

import com.ku_stacks.ku_ring.remote.club.request.ClubSubscribeRequest
import com.ku_stacks.ku_ring.remote.club.response.ClubDetailResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubListResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubSubscribeResponse
import com.ku_stacks.ku_ring.remote.club.response.ClubUnsubscribeResponse
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class ClubClient @Inject constructor(
    private val clubService: ClubService,
) {
    suspend fun subscribeClub(
        clubId: Int
    ): DefaultResponse<ClubSubscribeResponse> =
        clubService.subscribeClub(ClubSubscribeRequest(clubId))

    suspend fun unsubscribeClub(
        clubId: Int
    ): DefaultResponse<ClubUnsubscribeResponse> =
        clubService.unsubscribeClub(clubId)

    suspend fun getClubDetail(
        clubId: Int
    ): DefaultResponse<ClubDetailResponse> = clubService.getClubDetail(clubId)

    suspend fun getClubs(
        category: String?,
        division: String,
    ): DefaultResponse<ClubListResponse> = clubService.getClubs(category, division)

    suspend fun getSubscribedClubs(): DefaultResponse<ClubListResponse> =
        clubService.getSubscribedClubs()
}
