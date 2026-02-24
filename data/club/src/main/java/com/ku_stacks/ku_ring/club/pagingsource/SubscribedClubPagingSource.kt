package com.ku_stacks.ku_ring.club.pagingsource

import com.ku_stacks.ku_ring.remote.club.ClubClient
import com.ku_stacks.ku_ring.remote.club.response.ClubListItem
import com.ku_stacks.ku_ring.remote.util.BasePagingSource
import com.ku_stacks.ku_ring.remote.util.PagingRawData
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SubscribedClubPagingSource @AssistedInject constructor(
    private val client: ClubClient,
) : BasePagingSource<ClubListItem>() {
    @AssistedFactory
    interface Factory {
        fun create(): SubscribedClubPagingSource
    }

    override suspend fun provideData(cursor: Int, size: Int): PagingRawData<ClubListItem> {
        val response = client.getSubscribedClubs(cursor, size)
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
