package com.ku_stacks.ku_ring.auth.compose.state

sealed class VerifiedState {
    data object Initial : VerifiedState()
    data object Success : VerifiedState()
    data class Fail(val message: String?) : VerifiedState()
}