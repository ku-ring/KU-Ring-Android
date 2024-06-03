package com.ku_stacks.ku_ring

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ku_stacks.ku_ring.splash.SplashActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmokeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SplashActivity>()

    @Test
    fun testAppLaunch() {
        composeTestRule
            .onNodeWithContentDescription("스플래시 화면")
            .assertExists()
    }
}
