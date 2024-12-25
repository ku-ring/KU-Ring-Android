package com.ku_stacks.ku_ring.util

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <R> suspendRunCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
