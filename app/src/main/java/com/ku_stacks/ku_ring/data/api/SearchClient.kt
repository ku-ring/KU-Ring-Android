package com.ku_stacks.ku_ring.data.api

import com.ku_stacks.ku_ring.data.api.response.SearchStaffListResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchClient @Inject constructor(private val searchService: SearchService) {

    fun fetchStaffList(
        content: String
    ): Single<SearchStaffListResponse> = searchService.fetchStaffs(content)

}