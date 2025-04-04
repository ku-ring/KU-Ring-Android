package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password_check

/**
 * 비밀번호 및 비밀번호 확인란을 담은 컴포넌트입니다.
 *
 * @param state 비밀번호 및 비밀번호 확인란의 상태
 */
@Composable
internal fun PasswordInputGroup(
    state: PasswordInputState,
    modifier: Modifier = Modifier,
) {


    Column(modifier = modifier) {
        OutlinedSupportingTextField(
            query = state.password,
            onQueryUpdate = { state.password = it },
            textFieldState = state.passwordOutlinedTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp),
        )

        OutlinedSupportingTextField(
            query = state.passwordCheck,
            onQueryUpdate = { state.passwordCheck = it },
            textFieldState = state.passwordCheckOutlinedTextFieldState,
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
            PasswordInputGroup(
                state = rememberPasswordInputState(),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
