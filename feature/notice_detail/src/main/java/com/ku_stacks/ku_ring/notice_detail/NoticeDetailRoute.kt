package com.ku_stacks.ku_ring.notice_detail

import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.ui_util.KuringRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface NoticeDetailRoute : KuringRoute {
    @Serializable
    data class NoticeWeb(
        val url: String,
        val articleId: String,
        val id: Int,
        val category: String,
        val subject: String,
    ) : NoticeDetailRoute {
        companion object {
            // 현재 type-safe navigation이 nested object를 지원하지 않음
            // TODO: 매개변수로 webViewNotice를 직접 사용
            fun from(webViewNotice: WebViewNotice) = NoticeWeb(
                url = webViewNotice.url,
                articleId = webViewNotice.articleId,
                id = webViewNotice.id,
                category = webViewNotice.category,
                subject = webViewNotice.subject,
            )
        }

        fun toWebViewNotice(): WebViewNotice = WebViewNotice(
            url = url,
            articleId = articleId,
            id = id,
            category = category,
            subject = subject,
        )
    }

    @Serializable
    data class ReportComment(val commentId: Int) : NoticeDetailRoute
}