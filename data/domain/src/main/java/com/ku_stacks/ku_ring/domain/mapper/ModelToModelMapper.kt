package com.ku_stacks.ku_ring.domain.mapper

import com.ku_stacks.ku_ring.domain.Notice
import com.ku_stacks.ku_ring.domain.WebViewNotice

fun Notice.toWebViewNotice() = WebViewNotice(
    url = url,
    articleId = articleId,
    category = category
)