package com.ku_stacks.ku_ring

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ku_stacks.ku_ring.navigation.Navigator
import com.ku_stacks.ku_ring.navigation.keys.ArchiveKey
import com.ku_stacks.ku_ring.navigation.keys.ClubDetailKey
import com.ku_stacks.ku_ring.navigation.keys.ClubSubscriptionKey
import com.ku_stacks.ku_ring.navigation.keys.EditDepartmentsKey
import com.ku_stacks.ku_ring.navigation.keys.EditSubscriptionKey
import com.ku_stacks.ku_ring.navigation.keys.FeedbackKey
import com.ku_stacks.ku_ring.navigation.keys.LibrarySeatKey
import com.ku_stacks.ku_ring.navigation.keys.MainHubKey
import com.ku_stacks.ku_ring.navigation.keys.NoticeWebKey
import com.ku_stacks.ku_ring.navigation.keys.NotificationKey
import com.ku_stacks.ku_ring.navigation.keys.OpenSourceKey
import com.ku_stacks.ku_ring.navigation.keys.SearchKey
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Navigation3 마이그레이션 E2E 테스트.
 *
 * develop 브랜치 기준의 모든 화면이동 스펙이 현재 브랜치에서도 동일하게 동작하는지 검증한다.
 * HostActivity + NavDisplay + Navigator 기반의 실제 앱 환경에서 테스트한다.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HostActivity>()

    @Inject
    lateinit var navigator: Navigator

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // --- 앱 실행 ---

    @Test
    fun 앱이_실행되면_SplashKey가_초기_백스택에_존재한다() {
        assertEquals(SplashKey, navigator.backStack.first())
    }

    // --- 공지 탭 네비게이션 ---

    @Test
    fun 스플래시에서_메인허브로_이동하면_MainHubKey가_백스택에_존재한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
        }
        composeTestRule.waitForIdle()

        assertEquals(1, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack.first())
    }

    @Test
    fun 공지_탭에서_검색으로_이동하면_SearchKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(SearchKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(SearchKey, navigator.backStack.last())
    }

    @Test
    fun 공지_탭에서_아카이브로_이동하면_ArchiveKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(ArchiveKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(ArchiveKey, navigator.backStack.last())
    }

    @Test
    fun 공지_탭에서_알림_목록으로_이동하면_NotificationKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(NotificationKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(NotificationKey, navigator.backStack.last())
    }

    @Test
    fun 공지_탭에서_공지_상세로_이동하면_NoticeWebKey가_백스택_최상단에_위치한다() {
        val noticeKey = NoticeWebKey(
            url = "https://kuring.com/notice/1",
            articleId = "art1",
            id = "1",
            category = "학사",
            subject = "테스트공지",
        )
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(noticeKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(noticeKey, navigator.backStack.last())
    }

    @Test
    fun 공지_탭에서_학과_편집으로_이동하면_EditDepartmentsKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(EditDepartmentsKey)

            assertEquals(2, navigator.backStack.size)
            assertEquals(EditDepartmentsKey, navigator.backStack.last())
        }
    }

    // --- 동아리 탭 네비게이션 ---

    @Test
    fun 동아리_탭에서_구독_관리로_이동하면_ClubSubscriptionKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(ClubSubscriptionKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(ClubSubscriptionKey, navigator.backStack.last())
    }

    @Test
    fun 동아리_탭에서_동아리_상세로_이동하면_ClubDetailKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(ClubDetailKey(clubId = 42))

            assertEquals(2, navigator.backStack.size)
            assertEquals(ClubDetailKey(clubId = 42), navigator.backStack.last())
        }
    }

    // --- 설정 탭 네비게이션 ---

    @Test
    fun 설정에서_오픈소스_화면으로_이동하면_OpenSourceKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(OpenSourceKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(OpenSourceKey, navigator.backStack.last())
    }

    @Test
    fun 설정에서_피드백_화면으로_이동하면_FeedbackKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(FeedbackKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(FeedbackKey, navigator.backStack.last())
    }

    // --- 캠퍼스맵 탭 네비게이션 ---

    @Test
    fun 캠퍼스맵에서_열람실_좌석으로_이동하면_LibrarySeatKey가_백스택_최상단에_위치한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(LibrarySeatKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(LibrarySeatKey, navigator.backStack.last())
    }

    // --- 중첩 네비게이션 ---

    @Test
    fun 알림_목록에서_구독_편집으로_이동하면_올바른_백스택_순서가_유지된다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(NotificationKey)
            navigator.navigate(EditSubscriptionKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(3, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack[0])
        assertEquals(NotificationKey, navigator.backStack[1])
        assertEquals(EditSubscriptionKey, navigator.backStack[2])
    }

    @Test
    fun 구독_편집에서_학과_편집으로_이동하면_올바른_백스택_순서가_유지된다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(EditSubscriptionKey)
            navigator.navigate(EditDepartmentsKey)

            assertEquals(3, navigator.backStack.size)
            assertEquals(EditSubscriptionKey, navigator.backStack[1])
            assertEquals(EditDepartmentsKey, navigator.backStack[2])
        }
    }

    // --- 뒤로가기 ---

    @Test
    fun 상세_화면에서_뒤로가기를_하면_이전_화면으로_복원된다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
            navigator.navigate(SearchKey)
            navigator.navigate(NoticeWebKey("url", "art1", "1", "일반", "제목"))
        }
        composeTestRule.waitForIdle()
        assertEquals(3, navigator.backStack.size)

        composeTestRule.runOnUiThread { navigator.goBack() }
        composeTestRule.waitForIdle()
        assertEquals(SearchKey, navigator.backStack.last())

        composeTestRule.runOnUiThread { navigator.goBack() }
        composeTestRule.waitForIdle()
        assertEquals(MainHubKey(), navigator.backStack.last())
    }

    @Test
    fun 백스택에_항목이_하나만_남으면_뒤로가기가_실패한다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
        }
        composeTestRule.waitForIdle()

        composeTestRule.runOnUiThread {
            val result = navigator.goBack()
            assertEquals(false, result)
        }
        composeTestRule.waitForIdle()

        assertEquals(1, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack.first())
    }

    // --- 딥링크 ---

    @Test
    fun FCM_콜드_스타트_시_pendingDeepLink가_스플래시_완료_후_적용된다() {
        val noticeKey = NoticeWebKey(
            url = "https://kuring.com/notice/fcm",
            articleId = "fcm123",
            id = "99",
            category = "장학",
            subject = "FCM딥링크테스트",
        )

        composeTestRule.runOnUiThread {
            navigator.setPendingDeepLink(noticeKey)
            navigator.replaceAll(MainHubKey())
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(MainHubKey(), navigator.backStack[0])
        assertEquals(noticeKey, navigator.backStack[1])
    }

    @Test
    fun FCM_웜_스타트_시_직접_navigate하면_해당_화면이_백스택에_추가된다() {
        composeTestRule.runOnUiThread {
            navigator.replaceAll(MainHubKey())
        }
        composeTestRule.waitForIdle()

        val noticeKey = NoticeWebKey(
            url = "https://kuring.com/notice/warm",
            articleId = "warm456",
            id = "77",
            category = "일반",
            subject = "웜스타트딥링크",
        )
        composeTestRule.runOnUiThread {
            navigator.navigate(noticeKey)
        }
        composeTestRule.waitForIdle()

        assertEquals(2, navigator.backStack.size)
        assertEquals(noticeKey, navigator.backStack.last())
    }
}
