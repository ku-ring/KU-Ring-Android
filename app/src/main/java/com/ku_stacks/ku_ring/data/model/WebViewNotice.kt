package com.ku_stacks.ku_ring.data.model

import java.io.Serializable

data class WebViewNotice(
    val url: String,
    val articleId: String,
    val category: String,
): Serializable