package com.ku_stacks.ku_ring.auth.compose.signin.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.ku_stacks.ku_ring.auth.compose.component.button.RoundedCornerButton
import com.ku_stacks.ku_ring.auth.compose.component.textfield.PlainTextField
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.auth.compose.signin.SignInSideEffect
import com.ku_stacks.ku_ring.auth.compose.signin.SignInViewModel
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_button_sign_in
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_extra_option_find_password
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_extra_option_sign_up
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_text_field_placeholder_email
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_text_field_placeholder_password
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_in_top_bar_sub_heading
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SignInScreen(
    onNavigateUp: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    SignInSideEffect.NavigateToMain -> {
                        onNavigateToMain()
                    }
                }
            }
    }

    SignInScreen(
        id = viewModel.email,
        password = viewModel.password,
        isSignInFailed = viewModel.signInDialogState,
        onBackButtonClick = onNavigateUp,
        onIdTextFieldValueChange = viewModel::updateEmail,
        onPasswordTextFieldValueChange = viewModel::updatePassword,
        onSignInButtonClick = viewModel::signInUser,
        onSignUpButtonClick = onNavigateToSignUp,
        onFindPasswordButtonClick = onNavigateToFindPassword,
        onSignInDialogDismiss = { viewModel.updateSignInDialogState(false) },
        modifier = modifier
    )
}

@Composable
private fun SignInScreen(
    id: String,
    password: String,
    isSignInFailed: Boolean,
    onBackButtonClick: () -> Unit,
    onIdTextFieldValueChange: (String) -> Unit,
    onPasswordTextFieldValueChange: (String) -> Unit,
    onSignInButtonClick: () -> Unit,
    onSignUpButtonClick: () -> Unit,
    onFindPasswordButtonClick: () -> Unit,
    onSignInDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        Column(Modifier.fillMaxWidth()) {
            AuthTopBar(
                headingText = stringResource(sign_in_top_bar_heading),
                subHeadingText = stringResource(sign_in_top_bar_sub_heading),
                onBackButtonClick = onBackButtonClick,
            )
        }

        PlainTextField(
            query = id,
            onQueryUpdate = onIdTextFieldValueChange,
            placeholderText = stringResource(sign_in_text_field_placeholder_email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 45.dp),
        )

        PlainTextField(
            query = password,
            onQueryUpdate = onPasswordTextFieldValueChange,
            placeholderText = stringResource(sign_in_text_field_placeholder_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 8.dp),
        )

        RoundedCornerButton(
            text = stringResource(sign_in_button_sign_in),
            onClick = onSignInButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 30.dp),
        )

        SignInExtraOptionGroup(
            onSignUpButtonClick = onSignUpButtonClick,
            onFindPasswordButtonClick = onFindPasswordButtonClick,
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(44.dp),
        )
    }

    if (isSignInFailed) {
        SignInDialog(onDismissRequest = onSignInDialogDismiss)
    }
}

@Composable
private fun SignInExtraOptionGroup(
    onSignUpButtonClick: () -> Unit,
    onFindPasswordButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ExtraTextButton(
            text = stringResource(sign_in_extra_option_find_password),
            onClick = onFindPasswordButtonClick,
        )

        VerticalDivider(
            thickness = 1.dp,
            color = KuringTheme.colors.borderline,
            modifier = Modifier.padding(horizontal = 50.dp),
        )

        ExtraTextButton(
            text = stringResource(sign_in_extra_option_sign_up),
            onClick = onSignUpButtonClick,
        )
    }
}

@Composable
private fun ExtraTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = TextStyle(
            color = KuringTheme.colors.textCaption1,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight.Light,
        ),
        modifier = modifier
            .clickable { onClick() }
    )
}

@LightAndDarkPreview
@Composable
private fun SignInScreenPreview() {
    KuringTheme {
        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        SignInScreen(
            id = id,
            password = password,
            isSignInFailed = false,
            onBackButtonClick = {},
            onIdTextFieldValueChange = { id = it },
            onPasswordTextFieldValueChange = { password = it },
            onSignInButtonClick = {},
            onSignUpButtonClick = {},
            onFindPasswordButtonClick = {},
            onSignInDialogDismiss = {}
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SignInFailedScreenPreview() {
    KuringTheme {
        var id by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        SignInScreen(
            id = id,
            password = password,
            isSignInFailed = true,
            onBackButtonClick = {},
            onIdTextFieldValueChange = { id = it },
            onPasswordTextFieldValueChange = { password = it },
            onSignInButtonClick = {},
            onSignUpButtonClick = {},
            onFindPasswordButtonClick = {},
            onSignInDialogDismiss = {}
        )
    }
}
