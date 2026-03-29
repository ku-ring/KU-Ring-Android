package com.ku_stacks.ku_ring.navigation

import android.content.Context
import android.content.Intent
import com.ku_stacks.ku_ring.domain.WebViewNotice

interface DeepLinkIntentFactory {
    fun createMainIntent(context: Context, route: MainScreenRoute = MainScreenRoute.Notice): Intent
    fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        id: Int?,
        category: String?,
        subject: String?,
    ): Intent

    companion object {
        const val INTENT_KEY_ROUTE = "MAIN_SCREEN_ROUTE"
    }
}
