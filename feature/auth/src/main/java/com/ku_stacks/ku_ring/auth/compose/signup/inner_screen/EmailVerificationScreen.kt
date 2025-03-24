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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.auth.compose.component.CodeInputField
import com.ku_stacks.ku_ring.auth.compose.component.EmailInputGroup
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_button_proceed
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_navigate_to_ku_mail
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_top_bar_sub_heading


@Composable
internal fun EmailVerificationScreen(
    onNavigateUp: () -> Unit,
    onNavigateToPassword: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(false) }

    EmailVerificationScreen(
        email = email,
        code = code,
        isCodeSent = isCodeSent,
        isVerified = isVerified,
        onEmailChange = { email = it },
        onCodeChange = { code = it },
        onSendCodeClick = { isCodeSent = !isCodeSent },
        onVerifyCodeClick = { isVerified = !isVerified },
        onBackButtonClick = onNavigateUp,
        onKuMailClick = {},
        onProceedButtonClick = onNavigateToPassword,
        modifier = modifier,
    )
}

@Composable
internal fun EmailVerificationScreen(
    email: String,
    code: String,
    isCodeSent: Boolean,
    isVerified: Boolean,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onSendCodeClick: () -> Unit,
    onVerifyCodeClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onKuMailClick: () -> Unit,
    onProceedButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            isCodeSent = isCodeSent,
            modifier = Modifier
                .padding(top = 45.dp)
        )

        AnimatedVisibility(
            visible = isCodeSent,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CodeInputField(
                text = code,
                onTextChange = onCodeChange,
                onVerifyButtonClick = onVerifyCodeClick,
                modifier = Modifier
                    .padding(top = 8.dp)
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
            enabled = isVerified,
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
        var isCodeSent by remember { mutableStateOf(false) }
        var isVerified by remember { mutableStateOf(false) }

        EmailVerificationScreen(
            email = email,
            code = code,
            isCodeSent = isCodeSent,
            isVerified = isVerified,
            onEmailChange = { email = it },
            onCodeChange = { code = it },
            onSendCodeClick = { isCodeSent = !isCodeSent },
            onVerifyCodeClick = { isVerified = !isVerified },
            onBackButtonClick = { },
            onKuMailClick = {},
            onProceedButtonClick = { },
        )
    }
}