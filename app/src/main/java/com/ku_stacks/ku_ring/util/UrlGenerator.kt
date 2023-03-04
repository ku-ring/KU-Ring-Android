package com.ku_stacks.ku_ring.util

import timber.log.Timber

object UrlGenerator {

    @JvmStatic
    fun generateNoticeUrl(articleId: String, category: String, baseUrl: String): String {
        if (!category.isOnlyAlphabets()) {
            Timber.e("Only english category should be given: $category")
        }

        return if (category == "library") {
            "$baseUrl/$articleId"
        } else {
            "$baseUrl?id=$articleId"
        }
    }
}