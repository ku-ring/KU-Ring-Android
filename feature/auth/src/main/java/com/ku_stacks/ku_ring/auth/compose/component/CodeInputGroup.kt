package com.ku_stacks.ku_ring.auth.compose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.ku_stacks.ku_ring.auth.compose.component.button.VerificationButton
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedSupportingTextField
import com.ku_stacks.ku_ring.auth.compose.component.textfield.OutlinedTextFieldState
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.code_input_group_button
import com.ku_stacks.ku_ring.feature.auth.R.string.code_input_group_placeholder_code

private const val TEXT_FIELD_WEIGHT = 210/375f
private const val BUTTON_WEIGHT = 114/375f

@Composable
internal fun CodeInputField(
    text: String,
    onTextChange: (String) -> Unit,
    onVerifyButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    timeSuffix: @Composable (() -> Unit)? = null,
    textFieldState: OutlinedTextFieldState = OutlinedTextFieldState.Empty,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        OutlinedSupportingTextField(
            query = text,
            onQueryUpdate = onTextChange,
            placeholderText = stringResource(code_input_group_placeholder_code),
            textFieldState = textFieldState,
            suffix = timeSuffix,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.weight(TEXT_FIELD_WEIGHT),
        )

        VerificationButton(
            text = stringResource(code_input_group_button),
            onClick = onVerifyButtonClick,
            enabled = text.isNotBlank(),
            modifier = Modifier.weight(BUTTON_WEIGHT),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun CodeInputFieldPreview() {

    var code by remember { mutableStateOf("") }
    var isVerified by remember { mutableStateOf(false) }

    KuringTheme {
        CodeInputField(
            text = code,
            onTextChange = { code = it },
            onVerifyButtonClick = { isVerified = !isVerified },
            textFieldState = OutlinedTextFieldState.Correct("확인되었습니다."),
        )
    }
}