package com.ku_stacks.ku_ring.auth.compose.signout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignOutViewModel @Inject constructor(
) : ViewModel() {
    private val _sideEffect = Channel<SignOutSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun signOut() = viewModelScope.launch {
        // TODO: 회원탈퇴 API 호출
        _sideEffect.trySend(SignOutSideEffect.NavigateToSignOutComplete)
    }
}