package com.ku_stacks.ku_ring.benchmark

import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnboardingSwipeBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun swipeFeatureTabs() = benchmarkRule.measureRepeated(
        packageName = "com.ku_stacks.ku_ring",
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.res("feature_tab")), 20_000)
        },
    ) {
        val featureTabs = device.findObject(By.res("feature_tab"))

        featureTabs.swipe(Direction.LEFT, 1.0f)
        featureTabs.swipe(Direction.LEFT, 1.0f)
        featureTabs.swipe(Direction.RIGHT, 1.0f)
        featureTabs.swipe(Direction.RIGHT, 1.0f)

        device.waitForIdle()
    }
}