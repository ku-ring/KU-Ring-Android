package com.ku_stacks.ku_ring.util

import android.content.Intent
import com.ku_stacks.ku_ring.data.mapper.concatSubjectAndTag
import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.ui.notice_webview.NoticeWebActivity
import timber.log.Timber

fun Intent.putNoticeWebActivityExtras(notice: Notice) = this.putNoticeWebActivityExtras(
    notice.url,
    notice.articleId,
    notice.category,
    notice.postedDate,
    concatSubjectAndTag(notice.subject, notice.tag)
)

fun Intent.putNoticeWebActivityExtras(pushContent: PushContentUiModel) = this.apply {
    val (articleId, category, postedDate, subject, baseUrl, _, _, tag) = pushContent
    val url = UrlGenerator.generateNoticeUrl(articleId, category, baseUrl)

    putNoticeWebActivityExtras(
        url,
        articleId,
        category,
        postedDate,
        concatSubjectAndTag(subject, tag)
    )
}

fun Intent.putNoticeWebActivityExtras(
    url: String?,
    articleId: String?,
    category: String?,
    postedDate: String?,
    subject: String?,
) = this.apply {
    Timber.e("url : $url, category : $category")

    putExtra(NoticeWebActivity.NOTICE_URL, url)
    putExtra(NoticeWebActivity.NOTICE_ARTICLE_ID, articleId)
    putExtra(NoticeWebActivity.NOTICE_CATEGORY, category)
    putExtra(NoticeWebActivity.NOTICE_POSTED_DATE, postedDate)
    putExtra(NoticeWebActivity.NOTICE_SUBJECT, subject)
}