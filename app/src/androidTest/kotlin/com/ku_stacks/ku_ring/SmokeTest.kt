package com.ku_stacks.ku_ring

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ku_stacks.ku_ring.splash.SplashActivity
import com.ku_stacks.ku_ring.splash.SplashScreenState
import com.ku_stacks.ku_ring.splash.compose.SplashScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SmokeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Test
    fun testAppLaunch() {
        Intents.init()
        composeTestRule.setContent {
            val context = LocalContext.current
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            SplashScreen(
                screenState = SplashScreenState.LOADING,
                onUpdateApp = {},
                onDismissUpdateDialog = {},
                modifier = androidx.compose.ui.Modifier.fillMaxSize()
            )
        }
        Intents.intended(hasComponent(SplashActivity::class.java.name))
        Intents.release()
    }
}
