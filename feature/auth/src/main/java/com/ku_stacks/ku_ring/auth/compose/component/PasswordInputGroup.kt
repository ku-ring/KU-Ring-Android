package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password
import com.ku_stacks.ku_ring.feature.auth.R.string.reset_password_placeholder_password_check

/**
 * 비밀번호 및 비밀번호 확인란을 담은 컴포넌트입니다.
 *
 * @param state 비밀번호 및 비밀번호 확인란의 상태
 */
@Composable
internal fun PasswordInputGroup(
    state: PasswordInputGroupState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        PasswordInputTextField(
            query = state.password,
            onQueryUpdate = { state.password = it },
            textFieldState = state.passwordOutlinedTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp),
        )

        PasswordInputTextField(
            query = state.passwordCheck,
            onQueryUpdate = { state.passwordCheck = it },
            textFieldState = state.passwordCheckOutlinedTextFieldState,
            placeholderText = stringResource(reset_password_placeholder_password_check),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

@Composable
private fun PasswordInputTextField(
    query: String,
    onQueryUpdate: (String) -> Unit,
    textFieldState: OutlinedTextFieldState,
    placeholderText: String,
    modifier: Modifier = Modifier,
) {
    var textFieldVisibility by rememberSaveable { mutableStateOf(false) }
    val visualTransformation = if (textFieldVisibility) VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedSupportingTextField(
        query = query,
        onQueryUpdate = onQueryUpdate,
        textFieldState = textFieldState,
        placeholderText = placeholderText,
        visualTransformation = visualTransformation,
        suffix = {
            VisibilityControlButton(
                isVisible = textFieldVisibility,
                onClick = { textFieldVisibility = !textFieldVisibility }
            )
        },
        modifier = modifier
    )
}

@Composable
private fun VisibilityControlButton(
    isVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val iconRes = if (isVisible) R.drawable.ic_preview_close_v2
    else R.drawable.ic_preview_open_v2

    Icon(
        imageVector = ImageVector.vectorResource(iconRes),
        contentDescription = null,
        modifier = modifier
            .clickable(
                role = Role.Button,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple(false, Dp.Unspecified, Color.Black)
            ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PasswordInputGroupPreview() {
    KuringTheme {
        Column {
            PasswordInputGroup(
                state = rememberPasswordInputGroupState(),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}
