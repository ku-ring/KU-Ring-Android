package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState

@Composable
internal fun rememberPasswordInputGroupState(
    initialPassword: String = "",
    initialPasswordCheck: String = "",
): PasswordInputGroupState = rememberSaveable(saver = PasswordInputGroupState.Saver) {
    PasswordInputGroupState(
        initialPassword = initialPassword,
        initialPasswordCheck = initialPasswordCheck,
    )
}

internal class PasswordInputGroupState(
    private val initialPassword: String,
    private val initialPasswordCheck: String,
) {
    var password by mutableStateOf(initialPassword)
    var passwordCheck by mutableStateOf(initialPasswordCheck)

    private val isPasswordValid: Boolean
        get() = password.matches(passwordRegex)

    val passwordOutlinedTextFieldState: OutlinedTextFieldState
        get() = when {
            password.isBlank() -> OutlinedTextFieldState.Empty
            isPasswordValid -> OutlinedTextFieldState.Correct("")
            else -> OutlinedTextFieldState.Error(PASSWORD_ERROR_MESSAGE)
        }

    val passwordCheckOutlinedTextFieldState: OutlinedTextFieldState
        get() = when {
            passwordCheck.isBlank() -> OutlinedTextFieldState.Empty
            password == passwordCheck && isPasswordValid -> OutlinedTextFieldState.Correct("")
            else -> OutlinedTextFieldState.Error(PASSWORD_CHECK_ERROR_MESSAGE)
        }

    val passwordAllCorrect: Boolean
        get() = passwordOutlinedTextFieldState is OutlinedTextFieldState.Correct &&
                passwordCheckOutlinedTextFieldState is OutlinedTextFieldState.Correct


    companion object {
        private const val PASSWORD_ERROR_MESSAGE = "6~20자 영문 소문자, 숫자를 조합하여 입력해주세요."
        private const val PASSWORD_CHECK_ERROR_MESSAGE = "비밀번호가 일치하지 않습니다."
        private val passwordRegex = Regex("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{6,20}$")

        internal val Saver: Saver<PasswordInputGroupState, *> = listSaver(
            save = {
                listOf(
                    it.password,
                    it.passwordCheck,
                )
            },
            restore = {
                PasswordInputGroupState(
                    initialPassword = it[0],
                    initialPasswordCheck = it[1],
                )
            }
        )
    }
}
