package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.auth.compose.component.button.VerificationButton
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.email_input_group_button_email
import com.ku_stacks.ku_ring.feature.auth.R.string.email_input_group_button_resend
import com.ku_stacks.ku_ring.feature.auth.R.string.email_input_group_placeholder_email

private const val TEXT_FIELD_WEIGHT = 210 / 375f
private const val BUTTON_WEIGHT = 114 / 375f

@Composable
internal fun EmailInputGroup(
    text: String,
    onTextChange: (String) -> Unit,
    onSendButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    textFieldState: OutlinedTextFieldState = OutlinedTextFieldState.Empty,
    isCodeSent: Boolean = false, // TODO: ResetPassword 로직 구현 시 삭제
) {
    val buttonTextRes = remember(textFieldState) {
        if (textFieldState is OutlinedTextFieldState.Correct) email_input_group_button_resend
        else email_input_group_button_email
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        OutlinedSupportingTextField(
            query = text,
            onQueryUpdate = onTextChange,
            placeholderText = stringResource(email_input_group_placeholder_email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            textFieldState = textFieldState,
            modifier = Modifier.weight(TEXT_FIELD_WEIGHT)
        )

        VerificationButton(
            text = stringResource(buttonTextRes),
            onClick = onSendButtonClick,
            enabled = text.isNotBlank(),
            modifier = Modifier.weight(BUTTON_WEIGHT)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun EmailInputFieldPreview() {
    var email by remember { mutableStateOf("") }
    var isCodeSent by remember { mutableStateOf(false) }

    KuringTheme {
        EmailInputGroup(
            text = email,
            onTextChange = { email = it },
            onSendButtonClick = { isCodeSent = !isCodeSent },
            textFieldState = OutlinedTextFieldState.Correct(""),
            modifier = Modifier
        )
    }
}