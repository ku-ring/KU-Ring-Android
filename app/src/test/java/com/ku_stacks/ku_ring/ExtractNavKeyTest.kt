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
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5a206c73",
            articleId = "5a206c73",
            id = 20240315,
            category = "학사",
            subject = "2024학년도 1학기 수강신청 안내",
        )
        val intent = Intent().apply {
            putExtra(WebViewNotice.EXTRA_KEY, notice)
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(
            NoticeWebKey(
                url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5a206c73",
                articleId = "5a206c73",
                id = "20240315",
                category = "학사",
                subject = "2024학년도 1학기 수강신청 안내",
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
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=7b3e9f12",
            articleId = "7b3e9f12",
            id = 20240401,
            category = "일반",
            subject = "2024학년도 건국대학교 학생생활관 입사 안내",
        )
        val intent = Intent().apply {
            putExtra(WebViewNotice.EXTRA_KEY, notice)
            putExtra(DeepLinkIntentFactory.INTENT_KEY_ROUTE, "Calendar")
        }

        val result = HostActivity.extractNavKeyFromIntent(intent)

        assertEquals(
            NoticeWebKey(
                url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=7b3e9f12",
                articleId = "7b3e9f12",
                id = "20240401",
                category = "일반",
                subject = "2024학년도 건국대학교 학생생활관 입사 안내",
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
