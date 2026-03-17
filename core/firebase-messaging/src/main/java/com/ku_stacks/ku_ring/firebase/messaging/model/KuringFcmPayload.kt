package com.ku_stacks.ku_ring.firebase.messaging.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface KuringFcmPayload {
    @Serializable
    @SerialName("notice")
    data class Notice(
        val id: Int,
        val articleId: String,
        val category: String,
        val categoryKorName: String,
        val subject: String,
        val postedDate: String,
        val baseUrl: String,
    ) : KuringFcmPayload

    @Serializable
    @SerialName("admin")
    data class Custom(
        val title: String,
        val body: String,
    ) : KuringFcmPayload

    @Serializable
    @SerialName("academic")
    data class AcademicEvent(
        val title: String,
        val body: String,
    ) : KuringFcmPayload

    @Serializable
    @SerialName("club")
    data class Club(
        val clubId: Int,
        val title: String,
        val body: String,
    ) : KuringFcmPayload
}
