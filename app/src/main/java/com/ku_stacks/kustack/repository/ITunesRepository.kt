package com.ku_stacks.kustack.repository

import com.ku_stacks.kustack.data.api.ITunesClient
import com.ku_stacks.kustack.data.api.response.TrackListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ITunesRepository @Inject constructor(
    private val iTunesClient: ITunesClient
) {

    fun fetchTrackList(term: String, entity: String, limit: Int, offset: Int): Single<TrackListResponse> {
        return iTunesClient.fetchTrackList(term, entity, limit, offset)
    }

}