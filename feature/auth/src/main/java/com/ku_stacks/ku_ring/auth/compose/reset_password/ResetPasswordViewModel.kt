package com.ku_stacks.ku_ring.auth.compose.reset_password

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor() : ViewModel() {
    private val _sideEffect = Channel<ResetPasswordSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    var email by mutableStateOf("")
    var codeInputFieldEnable by mutableStateOf(false)
        private set

    fun updateEmail(email: String) {
        this.email = email
        codeInputFieldEnable = false
    }

    fun sendVerificationCode() = viewModelScope.launch {
        runCatching {
            // TODO: 이메일 확인 및 인증번호 전송 API 호출
            if (!email.endsWith("@konkuk.ac.kr")) throw Exception()
        }.onSuccess {
            codeInputFieldEnable = true
        }.onFailure {
            // TODO: 텍스트필드에 오류 메시지 표시
            Timber.e("current email: $email")
        }
    }

    fun verifyCode(code: String) = viewModelScope.launch {
        runCatching {
            // TODO: 인증번호 확인 API 호출
            if (code.length < 6 || code.length > 20) throw Exception()
        }.onSuccess {
            _sideEffect.send(ResetPasswordSideEffect.NavigateToResetPassword)
        }.onFailure {
            // TODO: 텍스트필드에 오류 메시지 표시
            Timber.e("current code: $code")
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
