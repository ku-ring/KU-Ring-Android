package com.ku_stacks.kustack.data.api

import com.ku_stacks.kustack.data.api.response.TrackListResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesService {

    @GET("/search?")
    fun fetchTrackList(
        @Query("term") term:String,
        @Query("entity") entity:String,
        @Query("limit") limit: Int,
        @Query("offset") offset:Int
    ): Single<TrackListResponse>

}