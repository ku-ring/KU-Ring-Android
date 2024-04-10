package com.ku_stacks.ku_ring.remote.space

import com.ku_stacks.ku_ring.remote.space.response.AppVersionResponse
import retrofit2.http.GET

interface KuringSpaceService {
    @GET("app-versions/android.json")
    suspend fun getMinimumAppVersion(): AppVersionResponse
}