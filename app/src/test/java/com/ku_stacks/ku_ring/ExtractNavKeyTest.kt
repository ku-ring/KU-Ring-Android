package com.ku_stacks.ku_ring

import android.app.Application
import android.content.Intent
import com.ku_stacks.ku_ring.domain.WebViewNotice
import com.ku_stacks.ku_ring.navigation.DeepLinkIntentFactory
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class ExtractNavKeyTest {

    @Test
    fun `빈 Intent를 전달하면 null을 반환한다`() {
        val intent = Intent()
        val result = HostActivity.extractNavKeyFromIntent(intent)
        assertNull(result)
    }

    @Test
    fun `WebViewNotice가 포함된 Intent를 전달하면 NoticeWebKey를 반환한다`() {
        val notice = WebViewNotice(
            url = "https://kuring.com/notice/123",
            articleId = "article123",
            id = 42,
            category = "학사",
            subject = "긴급공지",
        )
        val intent = Intent().apply {
            putExtra(WebViewNotice.EXTRA_KEY, notice)
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(
            NoticeWebKey(
                url = "https://kuring.com/notice/123",
                articleId = "article123",
                id = "42",
                category = "학사",
                subject = "긴급공지",
            ),
            result,
        )
    }

    @Test
    fun `MainScreenRoute 문자열이 포함된 Intent를 전달하면 MainHubKey를 반환한다`() {
        val intent = Intent().apply {
            putExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE, "Calendar")
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(MainHubKey(startTab = "Calendar"), result)
    }

    @Test
    fun `WebViewNotice와 Route가 모두 포함되면 WebViewNotice가 우선 적용된다`() {
        val notice = WebViewNotice(
            url = "https://kuring.com/notice/1",
            articleId = "art1",
            id = 1,
            category = "일반",
            subject = "제목",
        )
        val intent = Intent().apply {
            putExtra(WebViewNotice.EXTRA_KEY, notice)
            putExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE, "Calendar")
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(
            NoticeWebKey(
                url = "https://kuring.com/notice/1",
                articleId = "art1",
                id = "1",
                category = "일반",
                subject = "제목",
            ),
            result,
        )
    }

    @Test
    fun `Notice 탭 Route를 전달하면 Notice startTab의 MainHubKey를 반환한다`() {
        val intent = Intent().apply {
            putExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE, "Notice")
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(MainHubKey(startTab = "Notice"), result)
    }

    @Test
    fun `Club 탭 Route를 전달하면 Club startTab의 MainHubKey를 반환한다`() {
        val intent = Intent().apply {
            putExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE, "Club")
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(MainHubKey(startTab = "Club"), result)
    }
}
