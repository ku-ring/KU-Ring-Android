package com.ku_stacks.ku_ring.auth.compose.reset_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ku_stacks.ku_ring.auth.compose.state.VerifiedState
import com.ku_stacks.ku_ring.util.getHttpExceptionMessage
import com.ku_stacks.ku_ring.verification.repository.VerificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val verificationRepository: VerificationRepository
) : ViewModel() {
    private val _sideEffect = Channel<ResetPasswordSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    var email by mutableStateOf("")
    var emailVerifiedState by mutableStateOf<VerifiedState>(VerifiedState.Initial)
        private set
    var codeVerifiedState by mutableStateOf<VerifiedState>(VerifiedState.Initial)
        private set

    var codeInputFieldEnable by mutableStateOf(false)
        private set

    fun updateEmail(email: String) {
        this.email = email
        codeInputFieldEnable = false
    }

    private fun initializeVerifiedState() {
        emailVerifiedState = VerifiedState.Initial
        codeVerifiedState = VerifiedState.Initial
    }

    fun sendVerificationCode() = viewModelScope.launch {
        initializeVerifiedState()

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

    fun resetPassword(password: String) = viewModelScope.launch {
        runCatching {
            // TODO: 비밀번호 재설정 API 호출
            Timber.tag("ResetPasswordViewModel").d("current password: $password")
        }.onSuccess {
            _sideEffect.send(ResetPasswordSideEffect.NavigateToSignIn)
        }.onFailure(Timber::e)
    }
}
