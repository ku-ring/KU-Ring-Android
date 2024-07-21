package com.ku_stacks.ku_ring.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MemoryUsageMetric
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
class MainScreenMemoryBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    private val timeout = 10_000L

    @OptIn(ExperimentalMetricApi::class)
    @RequiresApi(Build.VERSION_CODES.Q)
    @Test
    fun mainScreenTabs() = benchmarkRule.measureRepeated(
        packageName = "com.ku_stacks.ku_ring",
        metrics = listOf(
            MemoryUsageMetric(
                MemoryUsageMetric.Mode.Last,
                listOf(MemoryUsageMetric.SubMetric.HeapSize)
            ),
            MemoryUsageMetric(MemoryUsageMetric.Mode.Max)
        ),
        iterations = 10,
        startupMode = StartupMode.WARM,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            device.wait(Until.hasObject(By.res("feature_tab")), timeout)
        },
    ) {
        device.findObject(By.res("skip_onboarding"))?.click()

        device.wait(Until.hasObject(By.res("notice_pager")), timeout)

        val pager = device.findObject(By.res("notice_pager"))
        repeat(8) {
            pager.swipe(Direction.LEFT, 1f)
        }
    }
}