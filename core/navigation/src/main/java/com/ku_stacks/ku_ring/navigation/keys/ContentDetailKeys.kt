package com.ku_stacks.ku_ring.navigation.keys

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data class NoticeWebKey(
    val url: String,
    val articleId: String,
    val id: String,
    val category: String,
    val subject: String,
) : NavKey

@Serializable data class NotionViewKey(val url: String) : NavKey

@Serializable data object ClubOnboardingKey : NavKey
@Serializable data class ClubDetailKey(val clubId: Int) : NavKey
@Serializable data object ClubSubscriptionKey : NavKey
