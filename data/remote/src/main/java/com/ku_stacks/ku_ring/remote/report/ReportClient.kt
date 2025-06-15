package com.ku_stacks.ku_ring.remote.report

import com.ku_stacks.ku_ring.remote.report.request.ReportRequest
import com.ku_stacks.ku_ring.remote.util.DefaultResponse
import javax.inject.Inject

class ReportClient @Inject constructor(
    private val reportService: ReportService,
) {
    suspend fun reportComment(
        targetId: Int,
        comment: String,
    ): DefaultResponse = reportService.report(ReportRequest.comment(targetId, comment))
}