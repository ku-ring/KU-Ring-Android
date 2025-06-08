package com.ku_stacks.ku_ring.remote.report.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ReportRequest private constructor(
    /**
     * Target comment id
     */
    @SerialName("targetId")
    val targetId: Int,
    /**
     * Type of the content which is reported.
     * ex) COMMENT
     */
    @SerialName("reportType")
    val reportType: String,
    /**
     * Short content of the report by the user.
     */
    @SerialName("content")
    val content: String,
) {
    companion object {
        private const val REPORT_TYPE_COMMENT = "COMMENT"
        fun comment(
            targetId: Int,
            content: String,
        ) = ReportRequest(
            targetId = targetId,
            reportType = REPORT_TYPE_COMMENT,
            content = content,
        )
    }
}