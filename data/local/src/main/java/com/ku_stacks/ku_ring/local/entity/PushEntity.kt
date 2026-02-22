package com.ku_stacks.ku_ring.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * 다양한 타입의 알림을 저장하기 위한 공통 Entity
 *
 * @param id 알림의 고유 ID
 * @param category 알림의 카테고리
 * @param receivedDate 알림을 받은 날짜
 * @param isNew 알림이 새로 들어온 알림인지 여부
 * @param content 알림 내용
 */
@Entity
class PushEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "receivedDate") val receivedDate: String,
    @ColumnInfo(name = "isNew") val isNew: Boolean,
    @ColumnInfo(name = "content") val content: PushContent,
)

@Serializable
sealed class PushContent {
    /**
     * 공지사항 알림을 구분하기 위한 타입
     */
    @Serializable
    data class Notice(
        val id: Int,
        val articleId: String,
        val category: String,
        val subject: String,
        val fullUrl: String,
        val postedDate: String,
    ) : PushContent()

    /**
     * 동아리 알림을 구분하기 위한 타입
     */
    @Serializable
    data class Club(
        val clubId: String,
        val title: String,
        val body: String,
    ) : PushContent()

    /**
     * 학사 일정과 커스텀 알림을 구분하기 위한 타입
     */
    @Serializable
    data class Common(
        val type: String,
        val title: String,
        val body: String,
    ) : PushContent()
}
