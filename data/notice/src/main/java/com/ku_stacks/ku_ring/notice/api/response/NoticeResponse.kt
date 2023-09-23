package com.ku_stacks.ku_ring.notice.api.response

import com.google.gson.annotations.SerializedName

data class NoticeResponse(
    @SerializedName(value = "articleId")
    val articleId: String,
    @SerializedName(value = "postedDate")
    val postedDate: String,
    @SerializedName(value = "subject")
    val subject: String,
    @SerializedName(value = "url")
    val url: String,
    @SerializedName(value = "category")
    val category: String,
    @SerializedName("important")
    val isImportant: Boolean,
) {
    companion object {
        fun mock() = NoticeResponse(
            articleId = "5b4a11b",
            postedDate = "20220203",
            subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
            category = "bachelor",
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
            isImportant = false,
        )
    }
}