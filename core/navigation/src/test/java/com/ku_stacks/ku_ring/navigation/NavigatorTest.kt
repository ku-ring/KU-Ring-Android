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
        val noticeKey = NoticeWebKey(
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=5a206c73",
            articleId = "5a206c73",
            id = "20240315001",
            category = "학사",
            subject = "2024학년도 1학기 수강신청 안내",
        )
        navigator.navigate(noticeKey)

        assertEquals(4, navigator.backStack.size)
        assertEquals(SplashKey, navigator.backStack[0])
        assertEquals(MainHubKey(), navigator.backStack[1])
        assertEquals(SearchKey, navigator.backStack[2])
        assertEquals(noticeKey, navigator.backStack[3])
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
        val deepLinkKey = NoticeWebKey(
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=a1c2d3e4",
            articleId = "a1c2d3e4",
            id = "20240520012",
            category = "장학",
            subject = "2024학년도 2학기 국가장학금 신청 안내",
        )
        navigator.setPendingDeepLink(deepLinkKey)

        navigator.replaceAll(MainHubKey())

        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack[0])
        assertEquals(deepLinkKey, navigator.backStack[1])
    }

    @Test
    fun `replaceAll 이후 pendingDeepLink는 소비되어 다음 replaceAll에 영향을 주지 않는다`() {
        val deepLinkKey = NoticeWebKey(
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=a1c2d3e4",
            articleId = "a1c2d3e4",
            id = "20240520012",
            category = "장학",
            subject = "2024학년도 2학기 국가장학금 신청 안내",
        )
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
        val noticeKey = NoticeWebKey(
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=7b3e9f12",
            articleId = "7b3e9f12",
            id = "20240401005",
            category = "일반",
            subject = "2024학년도 건국대학교 학생생활관 입사 안내",
        )
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
        navigator.navigate(
            NoticeWebKey(
                url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=c4d5e6f7",
                articleId = "c4d5e6f7",
                id = "20240410008",
                category = "학생",
                subject = "2024학년도 동아리 등록 및 지원금 신청 안내",
            )
        )

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
        val noticeKey = NoticeWebKey(
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=e8f9a0b1",
            articleId = "e8f9a0b1",
            id = "20240601001",
            category = "학사",
            subject = "2024학년도 1학기 기말고사 시간표 안내",
        )
        navigator.setPendingDeepLink(noticeKey)

        navigator.replaceAll(MainHubKey())

        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack[0])
        assertEquals(noticeKey, navigator.backStack[1])
    }

    @Test
    fun `웜 스타트 시 직접 navigate하면 해당 화면이 백스택에 추가된다`() {
        navigator.replaceAll(MainHubKey())

        val noticeKey = NoticeWebKey(
            url = "https://www.konkuk.ac.kr/do/MessageBoard/ArticleRead.do?id=b2c3d4e5",
            articleId = "b2c3d4e5",
            id = "20240715003",
            category = "장학",
            subject = "2024학년도 교내 성적우수 장학금 선발 안내",
        )
        navigator.navigate(noticeKey)

        assertEquals(2, navigator.backStack.size)
        assertEquals(noticeKey, navigator.backStack.last())
    }
}
