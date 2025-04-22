package com.ku_stacks.ku_ring.auth.compose.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    private var _signInDialogState by mutableStateOf(false)
    val signInDialogState get() = _signInDialogState

    private val _sideEffect = Channel<SignInSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateSignInDialogState(state: Boolean) {
        _signInDialogState = state
    }

    fun signInUser() = viewModelScope.launch {
        userRepository.signInUser(
            email = email,
            password = password
        ).onSuccess {
            _sideEffect.send(SignInSideEffect.NavigateToMain)
        }.onFailure {
            updateSignInDialogState(true)
        }
    }
}