package com.ku_stacks.ku_ring.club

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ku_stacks.ku_ring.club.mapper.toClub
import com.ku_stacks.ku_ring.club.mapper.toClubSummary
import com.ku_stacks.ku_ring.club.pagingsource.ClubListRequest
import com.ku_stacks.ku_ring.club.pagingsource.ClubsPagingSource
import com.ku_stacks.ku_ring.club.pagingsource.SubscribedClubPagingSource
import com.ku_stacks.ku_ring.domain.Club
import com.ku_stacks.ku_ring.domain.ClubCategory
import com.ku_stacks.ku_ring.domain.ClubDivision
import com.ku_stacks.ku_ring.domain.ClubSummary
import com.ku_stacks.ku_ring.domain.club.ClubRepository
import com.ku_stacks.ku_ring.remote.club.ClubClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val clubClient: ClubClient,
    private val clubsFactory: ClubsPagingSource.Factory,
    private val subscribedClubsFactory: SubscribedClubPagingSource.Factory,
) : ClubRepository {
    override suspend fun subscribeClub(clubId: Int): Result<Int> {
        val response = clubClient.subscribeClub(clubId)
        return when {
            response.isSuccessAndDataExists -> Result.success(response.data!!.bookmarkCount)
            else -> Result.failure(IllegalStateException(response.resultMsg))
        }
    }

    override suspend fun unsubscribeClub(clubId: Int): Result<Int> {
        val response = clubClient.unsubscribeClub(clubId)
        return when {
            response.isSuccessAndDataExists -> Result.success(response.data!!.bookmarkCount)
            else -> Result.failure(IllegalStateException(response.resultMsg))
        }
    }

    override suspend fun getClubDetail(clubId: Int): Result<Club> {
        val response = clubClient.getClubDetail(clubId)
        return when {
            response.isSuccessAndDataExists -> Result.success(response.data!!.toClub())
            else -> Result.failure(IllegalStateException(response.resultMsg))
        }
    }

    override fun getClubs(
        category: ClubCategory,
        division: ClubDivision,
        sortBy: String,
    ): Flow<PagingData<ClubSummary>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                clubsFactory.create(
                    ClubListRequest(
                        category = category.name.lowercase(),
                        division = division.name.lowercase(),
                        sortBy = sortBy,
                    )
                )
            },
        ).flow.map { pagingData ->
            pagingData.map { it.toClubSummary() }
        }
    }

    override fun getSubscribedClubs(): Flow<PagingData<ClubSummary>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { subscribedClubsFactory.create() },
        ).flow.map { pagingData ->
            pagingData.map { it.toClubSummary() }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
