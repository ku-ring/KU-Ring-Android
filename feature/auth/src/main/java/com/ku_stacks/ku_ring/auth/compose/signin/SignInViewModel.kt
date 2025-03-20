package com.ku_stacks.ku_ring.auth.compose.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    //TODO: 로직 구현시 지워질 예정
    private var _email by mutableStateOf("")
    val id get() = _email

    private var _password by mutableStateOf("")
    val password get() = _password

    private var _signInDialogState by mutableStateOf(false)
    val signInDialogState get() = _signInDialogState

    private val _sideEffect = Channel<SignInSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun updateEmail(id: String) {
        _email = id
    }

    fun updatePassword(password: String) {
        _password = password
    }

    fun updateSignInDialogState(state: Boolean) {
        _signInDialogState = state
    }

    fun signInUser() = viewModelScope.launch {
        userRepository.signUpUser(
            email = _email,
            password = _password
        ).onSuccess {
            _sideEffect.send(SignInSideEffect.NavigateToMain)
        }.onFailure {
            updateSignInDialogState(true)
        }
    }
}