package com.ku_stacks.ku_ring.navigator

import android.content.Context
import android.content.Intent
import com.ku_stacks.ku_ring.HostActivity
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.DeepLinkIntentFactory
import com.ku_stacks.ku_ring.navigation.MainScreenRoute
import javax.inject.Inject

class DeepLinkIntentFactoryImpl @Inject constructor() : DeepLinkIntentFactory {

    override fun createMainIntent(context: Context, route: MainScreenRoute): Intent {
        return Intent(context, HostActivity::class.java).apply {
            putExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE, route.route)
        }
    }

    override fun createNoticeWebIntent(
        context: Context,
        url: String?,
        articleId: String?,
        id: Int?,
        category: String?,
        subject: String?,
    ): Intent {
        if (url == null || articleId == null || category == null || id == null) {
            throw IllegalArgumentException("intent parameters shouldn't be null: $url, $articleId, $id, $category")
        }
        return Intent(context, HostActivity::class.java).apply {
            putExtra(
                WebViewNotice.EXTRA_KEY,
                WebViewNotice(url, articleId, id, category, subject.orEmpty()),
            )
        }
    }
}
