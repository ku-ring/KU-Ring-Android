package com.ku_stacks.ku_ring.navigation

import com.ku_stacks.ku_ring.navigation.keys.ArchiveKey
import com.ku_stacks.ku_ring.navigation.keys.EditDepartmentsKey
import com.ku_stacks.ku_ring.navigation.keys.EditSubscriptionKey
import com.ku_stacks.ku_ring.navigation.keys.FeedbackKey
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import com.ku_stacks.ku_ring.navigation.keys.NotificationKey
import com.ku_stacks.ku_ring.navigation.keys.SearchKey
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigatorTest {

    private lateinit var navigator: Navigator

    @Before
    fun setup() {
        navigator = Navigator()
    }

    // --- 기본 백스택 동작 ---

    @Test
    fun `Navigator가 생성되면 SplashKey가 초기 백스택에 존재한다`() {
        assertEquals(1, navigator.backStack.size)
        assertEquals(SplashKey, navigator.backStack.first())
    }

    @Test
    fun `navigate를 호출하면 백스택에 해당 키가 추가된다`() {
        navigator.navigate(MainHubKey())

        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack.last())
    }

    @Test
    fun `여러 화면을 순차적으로 navigate하면 올바른 순서로 백스택이 쌓인다`() {
        navigator.navigate(MainHubKey())
        navigator.navigate(SearchKey)
        navigator.navigate(NoticeWebKey("url", "art1", "1", "일반", "제목"))

        assertEquals(4, navigator.backStack.size)
        assertEquals(SplashKey, navigator.backStack[0])
        assertEquals(MainHubKey(), navigator.backStack[1])
        assertEquals(SearchKey, navigator.backStack[2])
        assertEquals(
            NoticeWebKey("url", "art1", "1", "일반", "제목"),
            navigator.backStack[3],
        )
    }

    @Test
    fun `goBack을 호출하면 마지막 항목이 제거되고 true를 반환한다`() {
        navigator.navigate(MainHubKey())
        navigator.navigate(SearchKey)

        val result = navigator.goBack()

        assertTrue(result)
        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack.last())
    }

    @Test
    fun `백스택에 항목이 하나만 남으면 goBack은 false를 반환한다`() {
        val result = navigator.goBack()

        assertFalse(result)
        assertEquals(1, navigator.backStack.size)
        assertEquals(SplashKey, navigator.backStack.first())
    }

    @Test
    fun `replaceAll을 호출하면 백스택이 새 키 하나로 교체된다`() {
        navigator.navigate(MainHubKey())
        navigator.navigate(SearchKey)

        navigator.replaceAll(MainHubKey())

        assertEquals(1, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack.first())
    }

    // --- 딥링크 ---

    @Test
    fun `setPendingDeepLink 후 replaceAll하면 딥링크 키가 백스택에 추가된다`() {
        val deepLinkKey = NoticeWebKey("url", "art1", "1", "일반", "제목")
        navigator.setPendingDeepLink(deepLinkKey)

        navigator.replaceAll(MainHubKey())

        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack[0])
        assertEquals(deepLinkKey, navigator.backStack[1])
    }

    @Test
    fun `replaceAll 이후 pendingDeepLink는 소비되어 다음 replaceAll에 영향을 주지 않는다`() {
        val deepLinkKey = NoticeWebKey("url", "art1", "1", "일반", "제목")
        navigator.setPendingDeepLink(deepLinkKey)

        navigator.replaceAll(MainHubKey())
        navigator.replaceAll(MainHubKey())

        assertEquals(1, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack.first())
    }

    // --- develop 브랜치 기준 네비게이션 경로 검증 ---

    @Test
    fun `공지 탭에서 검색 화면으로 이동하면 SearchKey가 백스택 최상단에 위치한다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(SearchKey)

        assertEquals(SearchKey, navigator.backStack.last())
    }

    @Test
    fun `공지 탭에서 아카이브 화면으로 이동하면 ArchiveKey가 백스택 최상단에 위치한다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(ArchiveKey)

        assertEquals(ArchiveKey, navigator.backStack.last())
    }

    @Test
    fun `공지 탭에서 알림 목록으로 이동하면 NotificationKey가 백스택 최상단에 위치한다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(NotificationKey)

        assertEquals(NotificationKey, navigator.backStack.last())
    }

    @Test
    fun `공지 탭에서 공지 상세로 이동하면 NoticeWebKey가 백스택 최상단에 위치한다`() {
        navigator.replaceAll(MainHubKey())
        val noticeKey = NoticeWebKey("https://example.com", "article1", "1", "일반", "공지제목")
        navigator.navigate(noticeKey)

        assertEquals(noticeKey, navigator.backStack.last())
    }

    @Test
    fun `공지 탭에서 학과 편집으로 이동하면 EditDepartmentsKey가 백스택 최상단에 위치한다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(EditDepartmentsKey)

        assertEquals(EditDepartmentsKey, navigator.backStack.last())
    }

    @Test
    fun `알림 목록에서 구독 편집으로 이동하면 올바른 백스택 순서가 유지된다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(NotificationKey)
        navigator.navigate(EditSubscriptionKey)

        assertEquals(3, navigator.backStack.size)
        assertEquals(EditSubscriptionKey, navigator.backStack.last())
    }

    @Test
    fun `구독 편집에서 학과 편집으로 이동하면 올바른 백스택 순서가 유지된다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(EditSubscriptionKey)
        navigator.navigate(EditDepartmentsKey)

        assertEquals(3, navigator.backStack.size)
        assertEquals(EditDepartmentsKey, navigator.backStack.last())
    }

    @Test
    fun `상세 화면에서 뒤로가기를 하면 이전 화면으로 복원된다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(SearchKey)
        navigator.navigate(NoticeWebKey("url", "a", "1", "c", "s"))

        navigator.goBack()
        assertEquals(SearchKey, navigator.backStack.last())

        navigator.goBack()
        assertEquals(MainHubKey(), navigator.backStack.last())
    }

    @Test
    fun `설정에서 피드백 화면으로 이동 후 뒤로가기하면 메인 허브로 돌아간다`() {
        navigator.replaceAll(MainHubKey())
        navigator.navigate(FeedbackKey)

        assertEquals(FeedbackKey, navigator.backStack.last())
        navigator.goBack()
        assertEquals(MainHubKey(), navigator.backStack.last())
    }

    @Test
    fun `FCM 콜드 스타트 시 pendingDeepLink가 스플래시 완료 후 적용된다`() {
        val noticeKey = NoticeWebKey("https://kuring.com/notice", "art123", "42", "학사", "긴급공지")
        navigator.setPendingDeepLink(noticeKey)

        navigator.replaceAll(MainHubKey())

        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack[0])
        assertEquals(noticeKey, navigator.backStack[1])
    }

    @Test
    fun `웜 스타트 시 직접 navigate하면 해당 화면이 백스택에 추가된다`() {
        navigator.replaceAll(MainHubKey())

        val noticeKey = NoticeWebKey("https://kuring.com/notice", "art456", "99", "장학", "장학금안내")
        navigator.navigate(noticeKey)

        assertEquals(2, navigator.backStack.size)
        assertEquals(noticeKey, navigator.backStack.last())
    }
}
