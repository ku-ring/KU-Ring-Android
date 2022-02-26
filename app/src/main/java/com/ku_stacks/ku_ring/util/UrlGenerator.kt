package com.ku_stacks.ku_ring.util

object UrlGenerator {

    @JvmStatic
    fun generateNoticeUrl(articleId: String, category: String, baseUrl: String): String {
        return if (category == "도서관") {
            "$baseUrl/$articleId"
        } else {
            "$baseUrl?id=$articleId"
        }
    }
}