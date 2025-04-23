package com.ku_stacks.ku_ring.auth.compose.component

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
import com.ku_stacks.ku_ring.auth.compose.state.VerifiedState
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.string.code_input_group_placeholder_code

@Composable
internal fun CodeInputField(
    text: String,
    onTextChange: (String) -> Unit,
    verifiedState: VerifiedState,
    modifier: Modifier = Modifier,
    timeSuffix: @Composable (() -> Unit)? = null,
) {
    val textFieldState = remember(verifiedState) {
        when (verifiedState) {
            is VerifiedState.Initial -> OutlinedTextFieldState.Empty
            is VerifiedState.Success -> OutlinedTextFieldState.Correct("")
            is VerifiedState.Fail -> OutlinedTextFieldState.Error(verifiedState.message ?: "")
        }
    }

    OutlinedSupportingTextField(
        query = text,
        onQueryUpdate = onTextChange,
        placeholderText = stringResource(code_input_group_placeholder_code),
        textFieldState = textFieldState,
        suffix = timeSuffix,
        visualTransformation = PasswordVisualTransformation(),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    )
}

@LightAndDarkPreview
@Composable
private fun CodeInputFieldPreview() {
    var code by remember { mutableStateOf("") }

    KuringTheme {
        CodeInputField(
            text = code,
            onTextChange = { code = it },
            verifiedState = VerifiedState.Initial,
        )
    }
}
