package com.ku_stacks.ku_ring.domain

data class Notice(
    val postedDate: String,
    val subject: String,
    val category: String,
    val department: String = "",
    val url: String,
    val articleId: String,
    var isNew: Boolean,
    var isRead: Boolean,
    var isSubscribing: Boolean,
    val isSaved: Boolean,
    val isReadOnStorage: Boolean,
    val tag: List<String>
) {
    companion object {
        fun mock() = Notice(
            postedDate = "20220203",
            subject = "2022학년도 1학기 재입학 합격자 유의사항 안내",
            category = "bachelor",
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5b4a11b",
            articleId = "5b4a11b",
            isNew = true,
            isRead = false,
            isSubscribing = false,
            isSaved = false,
            isReadOnStorage = false,
            tag = emptyList()
        )
    }
}