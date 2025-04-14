package com.ku_stacks.ku_ring.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.Locale

class KuringTimer(
    private val coroutineScope: CoroutineScope,
) {
    private var job: Job? = null

    private var leftTimeInSeconds by mutableIntStateOf(0)

    val isTimeUp: Boolean
        get() = leftTimeInSeconds <= 0

    private fun initializeTimer() {
        job?.cancel()
        leftTimeInSeconds = (DEFAULT_MINUTE * 60 + DEFAULT_SECOND)
    }

    fun startTimer() {
        initializeTimer()
        if (leftTimeInSeconds <= 0) return
        job = decreaseSecond().onEach {
            leftTimeInSeconds = it
        }.launchIn(coroutineScope)
    }

    fun stopTimer() {
        job?.cancel()
    }

    private fun decreaseSecond(): Flow<Int> = flow {
        var i = leftTimeInSeconds
        while (i > 0) {
            i--
            delay(1000)
            emit(i)
        }
    }

    fun getFormattedTime(): String {
        val timeFormatter = SimpleDateFormat("m:ss", Locale.getDefault())
        return timeFormatter.format(leftTimeInSeconds * 1000)
    }

    companion object {
        private const val DEFAULT_MINUTE = 0
        private const val DEFAULT_SECOND = 10
    }
}