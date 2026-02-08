package com.ku_stacks.ku_ring.club

import com.ku_stacks.ku_ring.club.mapper.toClub
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import com.ku_stacks.ku_ring.remote.club.ClubClient
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val clubClient: ClubClient,
) : ClubRepository {
    override suspend fun subscribeClub(clubId: Int): Result<Int> {
        val response = clubClient.subscribeClub(clubId)
        return when {
            response.isSuccess && response.data != null -> Result.success(response.data!!.bookmarkCount)
            else -> Result.failure(IllegalStateException("구독에 실패했습니다. 잠시 후 다시 시도해 주세요."))
        }
    }

    override suspend fun unsubscribeClub(clubId: Int): Result<Int> {
        val response = clubClient.unsubscribeClub(clubId)
        return when {
            response.isSuccess && response.data != null -> Result.success(response.data!!.bookmarkCount)
            else -> Result.failure(IllegalStateException("구독 해지에 실패했습니다. 잠시 후 다시 시도해 주세요."))
        }
    }

    override suspend fun getClubDetail(clubId: Int): Result<Club> {
        val response = clubClient.getClubDetail(clubId)
        return when {
            response.isSuccess && response.data != null -> Result.success(response.data!!.toClub())
            else -> Result.failure(IllegalStateException("동아리 정보를 불러오는데 실패했습니다. 잠시 후 다시 시도해 주세요."))
        }
    }
}