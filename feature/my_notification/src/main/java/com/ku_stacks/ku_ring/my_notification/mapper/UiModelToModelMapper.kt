package com.ku_stacks.ku_ring.my_notification.mapper

import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.util.WordConverter

fun PushContentUiModel.toWebViewNotice() = WebViewNotice(
    url = fullUrl,
    articleId = articleId,
    category = WordConverter.convertKoreanToEnglish(categoryKor),
    subject = subject,
)
