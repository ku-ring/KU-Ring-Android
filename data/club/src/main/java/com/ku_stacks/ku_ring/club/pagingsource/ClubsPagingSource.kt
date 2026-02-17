package com.ku_stacks.ku_ring.club.pagingsource

import com.ku_stacks.ku_ring.remote.club.ClubClient
import com.ku_stacks.ku_ring.remote.club.response.ClubListItem
import com.ku_stacks.ku_ring.remote.util.BasePagingSource
import com.ku_stacks.ku_ring.remote.util.PagingRawData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ClubsPagingSource @AssistedInject constructor(
    private val client: ClubClient,
    @Assisted private val request: ClubListRequest,
) : BasePagingSource<ClubListItem>() {
    @AssistedFactory
    interface Factory {
        fun create(request: ClubListRequest): ClubsPagingSource
    }

    override suspend fun provideData(cursor: Int, size: Int): PagingRawData<ClubListItem> {
        val response = client.getClubs(
            request.category,
            request.division,
            request.sortBy,
            cursor,
            size,
        )
        if (response.isSuccessAndDataExists) {
            val data = response.data!!
            return PagingRawData(
                items = data.clubs,
                hasNext = data.hasNext,
                nextCursor = data.cursor
            )
        } else {
            throw IllegalStateException(response.resultMsg)
        }
    }
}

data class ClubListRequest(val category: String, val division: String, val sortBy: String)
