package com.ku_stacks.ku_ring.push.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PushEntity(
    @PrimaryKey
    @ColumnInfo(name = "articleId") val articleId: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "postedDate") val postedDate: String,
    @ColumnInfo(name = "subject") val subject: String,
    @ColumnInfo(name = "baseUrl") val fullUrl: String,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
    @ColumnInfo(name = "receivedDate") val receivedDate: String
) {
    companion object {
        fun mock() = PushEntity(
            articleId = "ababab",
            category = "bachelor",
            postedDate = "2022-01-14 00:50:33",
            subject = "실감미디어 혁신 공유대학 융합전공 안내",
            fullUrl = "http://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?forum=notice&sort=6&id=ababab",
            isNew = true,
            receivedDate = "20220114-005036"
        )
    }
}