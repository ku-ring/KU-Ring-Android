package com.ku_stacks.ku_ring.data.mapper

import com.ku_stacks.ku_ring.data.model.Notice
import com.ku_stacks.ku_ring.data.model.WebViewNotice

fun Notice.toWebViewNotice() = WebViewNotice(
    url = url,
    articleId = articleId,
    category = category
)