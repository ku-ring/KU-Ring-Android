package com.ku_stacks.ku_ring.auth.compose.reset_password.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_button_proceed
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password_check
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_supporting_text_wrong
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_top_bar_sub_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_supporting_text_not_equal

@Composable
internal fun ReSetPasswordScreen(
    onNavigateUp: () -> Unit,
    onNavigateToSignUpComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }

    ReSetPasswordScreen(
        password = password,
        passwordCheck = passwordCheck,
        passwordTextFieldState = OutlinedTextFieldState.Empty,
        passwordCheckTextFieldState = OutlinedTextFieldState.Empty,
        isPasswordChecked = password == passwordCheck,
        onBackButtonClick = onNavigateUp,
        onPasswordChange = { password = it },
        onPasswordCheckChange = { passwordCheck = it },
        onProceedButtonClick = onNavigateToSignUpComplete,
        modifier = modifier,
    )
}

@Composable
private fun ReSetPasswordScreen(
    password: String,
    passwordCheck: String,
    passwordTextFieldState: OutlinedTextFieldState,
    passwordCheckTextFieldState: OutlinedTextFieldState,
    isPasswordChecked: Boolean,
    onBackButtonClick: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordCheckChange: (String) -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KuringTheme.colors.background)
    ) {
        AuthTopBar(
            headingText = stringResource(reset_password_top_bar_heading),
            subHeadingText = stringResource(reset_password_top_bar_sub_heading),
            onBackButtonClick = onBackButtonClick
        )

        OutlinedSupportingTextField(
            query = password,
            onQueryUpdate = onPasswordChange,
            textFieldState = passwordTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 45.dp),
        )

        OutlinedSupportingTextField(
            query = passwordCheck,
            onQueryUpdate = onPasswordCheckChange,
            textFieldState = passwordCheckTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password_check),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        KuringCallToAction(
            text = stringResource(reset_password_button_proceed),
            onClick = onProceedButtonClick,
            enabled = isPasswordChecked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SetPasswordScreenPreview() {
    var password by remember { mutableStateOf("") }
    var passwordCheck by remember { mutableStateOf("") }
    val passwordTextFieldState = if (password.isNotBlank() && password.length < 6) {
        OutlinedTextFieldState.Error(stringResource(reset_password_supporting_text_wrong))
    } else if (password.isNotBlank() && password == passwordCheck) {
        OutlinedTextFieldState.Correct("")
    } else {
        OutlinedTextFieldState.Empty
    }
    val passwordCheckTextFieldState = if (passwordCheck.isNotBlank() && password != passwordCheck) {
        OutlinedTextFieldState.Error(stringResource(reset_supporting_text_not_equal))
    } else if (passwordCheck.isNotBlank() && password == passwordCheck) {
        OutlinedTextFieldState.Correct("")
    } else {
        OutlinedTextFieldState.Empty
    }

    KuringTheme {
        ReSetPasswordScreen(
            password = password,
            passwordCheck = passwordCheck,
            passwordTextFieldState = passwordTextFieldState,
            passwordCheckTextFieldState = passwordCheckTextFieldState,
            isPasswordChecked = (password == passwordCheck) && password.isNotEmpty(),
            onBackButtonClick = {},
            onPasswordChange = { password = it },
            onPasswordCheckChange = { passwordCheck = it },
            onProceedButtonClick = {},
        )
    }
}