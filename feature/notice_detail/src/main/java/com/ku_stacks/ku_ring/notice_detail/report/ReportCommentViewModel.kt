package com.ku_stacks.ku_ring.notice_detail.report

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ku_stacks.ku_ring.domain.report.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportCommentViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
) : ViewModel() {

    var reportContent by mutableStateOf("")
        private set

    val textStatus by derivedStateOf {
        when (reportContent.length) {
            0 -> ReportCommentTextStatus.INITIAL
            in 1..MAX_REPORT_CONTENT_LENGTH -> ReportCommentTextStatus.NORMAL
            else -> ReportCommentTextStatus.TOO_LONG
        }
    }

    fun updateReportContent(content: String) {
        reportContent = content
    }

    suspend fun report(
        commentId: Int,
        content: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit,
    ) {
        reportRepository.reportComment(commentId, content)
            .onSuccess {
                onSuccess()
            }.onFailure {
                onFailure(it.message ?: "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.")
            }
    }

    companion object {
        const val MAX_REPORT_CONTENT_LENGTH = 256
    }
}