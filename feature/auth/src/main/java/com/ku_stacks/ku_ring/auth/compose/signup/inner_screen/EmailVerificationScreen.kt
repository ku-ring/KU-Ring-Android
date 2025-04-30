package com.ku_stacks.ku_ring.auth.compose.signup.inner_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ku_stacks.ku_ring.auth.compose.component.CodeInputField
import com.ku_stacks.ku_ring.auth.compose.component.CodeTimer
import com.ku_stacks.ku_ring.auth.compose.component.EmailInputGroup
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.auth.compose.signup.SignUpSideEffect
import com.ku_stacks.ku_ring.auth.compose.signup.SignUpViewModel
import com.ku_stacks.ku_ring.auth.compose.state.VerifiedState
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_button_proceed
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_navigate_to_ku_mail
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_top_bar_sub_heading
import com.ku_stacks.ku_ring.util.KuringTimer
import com.ku_stacks.ku_ring.util.navigateToExternalBrowser
import kotlinx.coroutines.launch

private const val KU_MAIL_URL = "https://kumail.konkuk.ac.kr/"

@Composable
internal fun EmailVerificationScreen(
    onNavigateUp: () -> Unit,
    onNavigateToPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    DisposableEffect(viewModel.sideEffect, lifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
                .collect { sideEffect ->
                    if (sideEffect is SignUpSideEffect.NavigateToSetPassword) {
                        onNavigateToPassword()
                    }
                }
        }
        onDispose {
            with(viewModel) {
                initializeVerifiedState()
                updateCode("")
            }
        }
    }

    EmailVerificationScreen(
        email = viewModel.email,
        emailVerifiedState = viewModel.emailVerifiedState,
        codeVerifiedState = viewModel.codeVerifiedState,
        onEmailChange = viewModel::updateEmail,
        code = viewModel.code,
        onCodeChange = viewModel::updateCode,
        onSendCodeClick = {
            viewModel.sendVerificationCode()
        },
        onBackButtonClick = onNavigateUp,
        onKuMailClick = { context.navigateToExternalBrowser(KU_MAIL_URL) },
        onProceedButtonClick = viewModel::verifyVerificationCode,
        modifier = modifier,
    )
}

@Composable
internal fun EmailVerificationScreen(
    email: String,
    code: String,
    emailVerifiedState: VerifiedState,
    codeVerifiedState: VerifiedState,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onSendCodeClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onKuMailClick: () -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val timer = remember { KuringTimer(coroutineScope) }

    val codeInputFieldEnable = remember(emailVerifiedState) {
        emailVerifiedState is VerifiedState.Success
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = KuringTheme.colors.background)
    ) {
        AuthTopBar(
            headingText = stringResource(sign_up_top_bar_heading),
            subHeadingText = stringResource(sign_up_top_bar_sub_heading),
            onBackButtonClick = onBackButtonClick,
        )

        EmailInputGroup(
            text = email,
            onTextChange = onEmailChange,
            onSendButtonClick = onSendCodeClick,
            verifiedState = emailVerifiedState,
            modifier = Modifier
                .padding(top = 45.dp)
        )

        AnimatedVisibility(
            visible = codeInputFieldEnable,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CodeInputField(
                text = code,
                onTextChange = onCodeChange,
                verifiedState = codeVerifiedState,
                modifier = Modifier.padding(top = 8.dp),
                timeSuffix = {
                    CodeTimer(
                        timer = timer,
                        enabled = codeInputFieldEnable
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(sign_up_navigate_to_ku_mail),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                color = KuringTheme.colors.textCaption1
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onKuMailClick,
                ),
        )

        KuringCallToAction(
            text = stringResource(sign_up_button_proceed),
            onClick = onProceedButtonClick,
            enabled = codeInputFieldEnable && code.isNotBlank() && !timer.isTimeUp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun EmailVerificationScreenPreview() {
    KuringTheme {
        var email by remember { mutableStateOf("") }
        var code by remember { mutableStateOf("") }
        var codeInputFieldEnable by remember { mutableStateOf(false) }

        EmailVerificationScreen(
            email = email,
            emailVerifiedState = VerifiedState.Initial,
            codeVerifiedState = VerifiedState.Initial,
            onEmailChange = { email = it },
            onSendCodeClick = { codeInputFieldEnable = !codeInputFieldEnable },
            onBackButtonClick = { },
            onKuMailClick = {},
            onProceedButtonClick = { },
            code = code,
            onCodeChange = { code = it },
        )
    }
}