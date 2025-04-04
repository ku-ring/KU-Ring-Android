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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.ku_stacks.ku_ring.auth.compose.component.PasswordInputGroup
import com.ku_stacks.ku_ring.auth.compose.component.PasswordInputGroupState
import com.ku_stacks.ku_ring.auth.compose.component.rememberPasswordInputGroupState
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.auth.compose.reset_password.ResetPasswordSideEffect
import com.ku_stacks.ku_ring.auth.compose.reset_password.ResetPasswordViewModel
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_button_proceed
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_top_bar_sub_heading

@Composable
internal fun ResetPasswordScreen(
    onNavigateUp: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val passwordInputState = rememberPasswordInputGroupState()

    LaunchedEffect(viewModel.sideEffect, lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                if (sideEffect is ResetPasswordSideEffect.NavigateToSignIn) {
                    onNavigateToSignIn()
                }
            }
    }

    ResetPasswordScreen(
        passwordInputState = passwordInputState,
        onBackButtonClick = onNavigateUp,
        onProceedButtonClick = { viewModel.resetPassword(passwordInputState.password) },
        modifier = modifier,
    )
}

@Composable
private fun ResetPasswordScreen(
    passwordInputState: PasswordInputGroupState,
    onBackButtonClick: () -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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

        PasswordInputGroup(
            state = passwordInputState,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        KuringCallToAction(
            text = stringResource(reset_password_button_proceed),
            onClick = onProceedButtonClick,
            enabled = passwordInputState.passwordAllCorrect,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SetPasswordScreenPreview() {
    KuringTheme {
        ResetPasswordScreen(
            passwordInputState = rememberPasswordInputGroupState(),
            onBackButtonClick = {},
            onProceedButtonClick = {},
        )
    }
}