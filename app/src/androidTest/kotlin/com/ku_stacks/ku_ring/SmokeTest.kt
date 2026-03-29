package com.ku_stacks.ku_ring

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ku_stacks.ku_ring.navigation.Navigator
import com.ku_stacks.ku_ring.navigation.keys.SplashKey
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SmokeTest {

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

    @Test
    fun 앱이_실행되면_HostActivity가_정상적으로_생성된다() {
        assertTrue(composeTestRule.activity is HostActivity)
    }

    @Test
    fun 앱이_실행되면_초기_백스택에_SplashKey가_존재한다() {
        assertEquals(SplashKey, navigator.backStack.first())
    }
}
