package com.ku_stacks.ku_ring.report

import com.ku_stacks.ku_ring.domain.report.ReportCommentResult
import com.ku_stacks.ku_ring.domain.report.ReportRepository
import com.ku_stacks.ku_ring.remote.report.ReportClient
import com.ku_stacks.ku_ring.util.suspendRunCatching
import retrofit2.HttpException
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportClient: ReportClient,
) : ReportRepository {
    override suspend fun reportComment(
        commentId: Int,
        reportContent: String
    ): Result<Unit> = suspendRunCatching {
        return try {
            when (reportClient.reportComment(commentId, reportContent).resultCode) {
                ReportCommentResult.REPORT_SUCCESS -> Result.success(Unit)
                ReportCommentResult.COMMENT_NOT_EXIST -> Result.failure(IllegalArgumentException("댓글이 존재하지 않습니다."))
                else -> Result.failure(IllegalAccessException("댓글 신고에 실패했습니다. 잠시 후 다시 시도해 주세요."))
            }
        } catch (e: HttpException) {
            // DefaultResponse가 아닌 응답: 댓글이 없는 경우
            Result.failure(IllegalAccessException("댓글이 존재하지 않습니다."))
        }
    }
}