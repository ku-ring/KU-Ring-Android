package com.ku_stacks.ku_ring.auth.compose.component.textfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.textfield.KuringTextField
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

/**
 * 텍스트 입력만 수행하는 기본 텍스트필드입니다.
 *
 * @param query TextField의 값
 * @param onQueryUpdate TextField의 값을 업데이트하는 콜백
 * @param modifier [Modifier]
 * @param placeholderText 힌트로 표시되는 문자열입니다
 * @param shape TextField의 모양입니다.
 * @param keyboardOptions 키보드의 입력 옵션입니다.
 * @param keyboardActions 키보드의 액션입니다.
 * @param interactionSource [MutableInteractionSource]
 */

@Composable
fun PlainTextField(
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    shape: Shape = RoundedCornerShape(8.dp),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 13.dp)
    KuringTextField(
        query = query,
        onQueryUpdate = onQueryUpdate,
        modifier = modifier,
        shape = shape,
        placeholderText = placeholderText,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        visualTransformation = visualTransformation,
        contentPadding = contentPadding,
    )
}

@LightAndDarkPreview
@Composable
private fun PlainTextFieldPreview() {
    KuringTheme {
        var emailQuery by remember { mutableStateOf("") }
        var pwQuery by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            PlainTextField(
                query = emailQuery,
                onQueryUpdate = { emailQuery = it },
                placeholderText = "학교 이메일 주소",
                modifier = Modifier.fillMaxWidth(),
            )

            PlainTextField(
                query = pwQuery,
                onQueryUpdate = { pwQuery = it },
                placeholderText = "6~20자 영문 소문자+숫자",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )
        }
    }
}
