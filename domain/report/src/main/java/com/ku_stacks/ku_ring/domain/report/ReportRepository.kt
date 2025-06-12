package com.ku_stacks.ku_ring.domain.report

interface ReportRepository {
    suspend fun reportComment(
        commentId: Int,
        reportContent: String,
    ): Result<Unit>
}