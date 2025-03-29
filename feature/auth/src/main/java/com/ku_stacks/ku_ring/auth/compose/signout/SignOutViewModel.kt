package com.ku_stacks.ku_ring.auth.compose.signout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
) : ViewModel() {
    var isSignOutSuccess: Boolean by mutableStateOf(false)
        private set

    fun signOut() {
        // TODO: 회원탈퇴 API 호출
        isSignOutSuccess = true
    }
}