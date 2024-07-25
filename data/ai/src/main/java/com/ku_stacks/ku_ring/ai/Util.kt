package com.ku_stacks.ku_ring.ai

import java.time.ZoneOffset

private const val SSE_PREFIX = "data:"

internal fun String.isSSEData(): Boolean = startsWith(SSE_PREFIX)

internal fun String.parseSSEData(): String = removePrefix(SSE_PREFIX)

internal fun systemDefaultZone() = ZoneOffset.of(ZoneOffset.systemDefault().id)