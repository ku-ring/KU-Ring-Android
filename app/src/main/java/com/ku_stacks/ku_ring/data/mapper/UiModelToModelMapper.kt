package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.model.WebViewNotice
import com.ku_stacks.ku_ring.ui.my_notification.ui_model.PushContentUiModel
import com.ku_stacks.ku_ring.util.WordConverter

fun PushContentUiModel.toWebViewNotice() = WebViewNotice(
    url = fullUrl,
    articleId = articleId,
    category = WordConverter.convertKoreanToEnglish(categoryKor),
)