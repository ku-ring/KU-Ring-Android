package com.ku_stacks.ku_ring.remote.report

import com.ku_stacks.ku_ring.remote.report.request.ReportRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportService {
    @POST("v2/reports")
    suspend fun report(
        @Body reportBody: ReportRequest,
    ): DefaultResponse
}