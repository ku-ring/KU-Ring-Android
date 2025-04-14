package com.ku_stacks.ku_ring.auth.compose.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.domain.user.repository.UserRepository
import com.ku_stacks.ku_ring.util.getHttpExceptionMessage
import com.ku_stacks.ku_ring.verification.repository.VerificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val verificationRepository: VerificationRepository
) : ViewModel() {
    private val _sideEffect = Channel<SignUpSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    var email by mutableStateOf("")
    var emailVerifiedState by mutableStateOf<VerifiedState>(VerifiedState.Initial)
    var codeVerifiedState by mutableStateOf<VerifiedState>(VerifiedState.Initial)

    fun sendVerificationCode() = viewModelScope.launch {
        emailVerifiedState = VerifiedState.Initial
        verificationRepository.sendVerificationCode(email)
            .onSuccess {
                emailVerifiedState = VerifiedState.Success
            }
            .onFailure { exception ->
                val message = exception.getHttpExceptionMessage()
                emailVerifiedState = VerifiedState.Fail(message)
            }
    }

    fun verifyVerificationCode(code: String) = viewModelScope.launch {
        verificationRepository.verifyCode(email = email, code = code)
            .onSuccess {
                codeVerifiedState = VerifiedState.Success
            }
            .onFailure { exception ->
                val message = exception.getHttpExceptionMessage()
                codeVerifiedState = VerifiedState.Fail(message)
            }
    }

    fun signUpUser(password: String) = viewModelScope.launch {
        userRepository.signUpUser(email, password)
            .onSuccess {

            }.onFailure(Timber::e)
    }
}

sealed class VerifiedState {
    data object Initial : VerifiedState()
    data object Success : VerifiedState()
    data class Fail(val message: String?) : VerifiedState()
}