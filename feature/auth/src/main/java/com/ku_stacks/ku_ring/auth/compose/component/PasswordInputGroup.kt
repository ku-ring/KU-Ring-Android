package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password_check
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_supporting_text_wrong
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_supporting_text_not_equal

/**
 * 비밀번호 및 비밀번호 확인란을 담은 컴포넌트입니다.
 *
 * 비밀번호와 비밀번호 확인 문자열, 문자열 변경 함수를 받아 각각의 상태를 관리하고,
 * [onConditionsPass] 콜백 함수를 통해 통과 여부를 반환받을 수 있습니다.
 */
@Composable
internal fun PasswordInputGroup(
    password: String,
    isPasswordValid: Boolean,
    onPasswordChange: (String) -> Unit,
    passwordCheck: String,
    onPasswordCheckChange: (String) -> Unit,
    onConditionsPass: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val passwordTextFieldState = remember(password, isPasswordValid) {
        when {
            password.isBlank() -> OutlinedTextFieldState.Empty
            isPasswordValid -> OutlinedTextFieldState.Correct("")
            else -> OutlinedTextFieldState.Error(
                context.getString(
                    reset_password_supporting_text_wrong
                )
            )
        }
    }
    val passwordCheckTextFieldState = remember(password, passwordCheck, isPasswordValid) {
        when {
            passwordCheck.isBlank() -> OutlinedTextFieldState.Empty
            password == passwordCheck && isPasswordValid -> OutlinedTextFieldState.Correct("")
            else -> OutlinedTextFieldState.Error(context.getString(reset_supporting_text_not_equal))
        }
    }

    LaunchedEffect(passwordTextFieldState, passwordCheckTextFieldState) {
        val isProceedEnabled =
            passwordTextFieldState is OutlinedTextFieldState.Correct && passwordCheckTextFieldState is OutlinedTextFieldState.Correct
        onConditionsPass(isProceedEnabled)
    }

    Column(modifier = modifier) {
        OutlinedSupportingTextField(
            query = password,
            onQueryUpdate = onPasswordChange,
            textFieldState = passwordTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp),
        )

        OutlinedSupportingTextField(
            query = passwordCheck,
            onQueryUpdate = onPasswordCheckChange,
            textFieldState = passwordCheckTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password_check),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordInputGroupPreview() {
    KuringTheme {
        Column {
            var password by remember { mutableStateOf("") }
            var passwordCheck by remember { mutableStateOf("") }
            var proceedEnabled by remember { mutableStateOf(false) }
            PasswordInputGroup(
                password = password,
                isPasswordValid = password.length > 3,
                onPasswordChange = { password = it },
                passwordCheck = passwordCheck,
                onPasswordCheckChange = { passwordCheck = it },
                onConditionsPass = { proceedEnabled = it },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            KuringCallToAction(
                text = "확인",
                onClick = {},
                enabled = proceedEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
        }
    }
}