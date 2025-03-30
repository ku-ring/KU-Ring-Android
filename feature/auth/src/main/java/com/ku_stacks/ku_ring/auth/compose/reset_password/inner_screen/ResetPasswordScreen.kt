package com.ku_stacks.ku_ring.auth.compose.reset_password.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.auth.compose.reset_password.ResetPasswordSideEffect
import com.ku_stacks.ku_ring.auth.compose.reset_password.ResetPasswordViewModel
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
internal fun ResetPasswordScreen(
    onNavigateUp: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val isPasswordValid: Boolean = with(viewModel) {
        remember(viewModel.password) { checkPassword() }
    }

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                if (sideEffect is ResetPasswordSideEffect.NavigateToSignIn) {
                    onNavigateToSignIn()
                }
            }
    }

    ResetPasswordScreen(
        password = viewModel.password,
        isPasswordValid = isPasswordValid,
        onPasswordChange = { viewModel.password = it },
        onBackButtonClick = onNavigateUp,
        onProceedButtonClick = viewModel::resetPassword,
        modifier = modifier,
    )
}

@Composable
private fun ResetPasswordScreen(
    password: String,
    isPasswordValid: Boolean,
    onPasswordChange: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var passwordCheck by rememberSaveable { mutableStateOf("") }

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

    val proceedButtonEnabled = remember(passwordTextFieldState, passwordCheckTextFieldState) {
        passwordTextFieldState is OutlinedTextFieldState.Correct && passwordCheckTextFieldState is OutlinedTextFieldState.Correct
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KuringTheme.colors.background)
            .imePadding()
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
            onQueryUpdate = { passwordCheck = it },
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
            enabled = proceedButtonEnabled,
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

    KuringTheme {
        ResetPasswordScreen(
            password = password,
            isPasswordValid = password.isNotBlank(),
            onBackButtonClick = {},
            onPasswordChange = { password = it },
            onProceedButtonClick = {},
        )
    }
}