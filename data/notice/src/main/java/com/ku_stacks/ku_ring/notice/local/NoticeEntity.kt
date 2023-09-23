package com.ku_stacks.ku_ring.notice.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticeEntity(
    @PrimaryKey @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "department") val department: String = "",
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "postedDate") val postedDate: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
    @ColumnInfo(name = "isRead") val isRead: Boolean,
    @ColumnInfo(name = "isSaved") val isSaved: Boolean,
    @ColumnInfo(name = "isReadOnStorage") val isReadOnStorage: Boolean,
) {
    companion object {
        fun mock() = NoticeEntity(
            articleId = "5b4a11b",
            category = "bachelor",
            subject = "2023학년도 전과 선발자 안내",
            postedDate = "20230208",
            url = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=5b4f972&cat=0000300001",
            isNew = false,
            isRead = false,
            isSaved = false,
            isReadOnStorage = false,
        )

        fun mockRead() = mock().copy(isRead = true)

        fun mockDepartmentNotice()= NoticeEntity(
            articleId = "182677",
            category = "department",
            subject = "2023학년도 진로총조사 설문 요청",
            department = "cse",
            postedDate = "2023-05-02",
            url = "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&seq=182677",
            isNew = false,
            isRead = false,
            isSaved = false,
            isReadOnStorage = false,
        )
    }
}