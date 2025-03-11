package com.ku_stacks.ku_ring.auth.compose.component.textfield

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.auth.compose.component.textfield.TextFieldState.Correct
import com.ku_stacks.ku_ring.auth.compose.component.textfield.TextFieldState.Empty
import com.ku_stacks.ku_ring.auth.compose.component.textfield.TextFieldState.Error
import com.ku_stacks.ku_ring.designsystem.components.textfield.KuringTextField
import com.ku_stacks.ku_ring.designsystem.components.textfield.KuringTextFieldDefault
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

/**
 * [TextFieldState] 값에 따라 테두리 색상이 바뀌는 TextField 컴포넌트입니다.
 *
 * @param query TextField의 값
 * @param onQueryUpdate TextField의 값을 업데이트하는 콜백
 * @param modifier [Modifier]
 * @param placeholderText 힌트로 표시되는 문자열입니다
 * @param textFieldState [TextFieldState]
 * @param suffix innerTextField의 후행 요소입니다.
 * @param keyboardOptions 키보드의 입력 옵션입니다.
 * @param keyboardActions 키보드의 액션입니다.
 * @param interactionSource [MutableInteractionSource]
 * @param visualTransformation [VisualTransformation]
 */

@Composable
fun OutlinedSupportingTextField(
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    textFieldState: TextFieldState = Empty,
    suffix: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val textStyle = KuringTextFieldDefault.textStyle()
    val shape: Shape = RoundedCornerShape(8.dp)
    val supportingTextColor = textFieldState.getColor()
    val supportingText = textFieldState.getText()
    val borderModifier: Modifier = if (textFieldState != Empty) {
        Modifier.border(width = 1.dp, color = supportingTextColor, shape = shape)
    } else {
        Modifier
    }

    Column(modifier = modifier) {
        KuringTextField(
            query = query,
            onQueryUpdate = onQueryUpdate,
            modifier = Modifier
                .fillMaxWidth()
                .then(borderModifier),
            shape = shape,
            textStyle = textStyle,
            placeholderText = placeholderText,
            suffix = suffix,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(vertical = 13.dp, horizontal = 16.dp),
            visualTransformation = visualTransformation,
        )

        if (!supportingText.isNullOrBlank()) {
            Text(
                text = supportingText,
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 16.3.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = supportingTextColor,
                ),
                modifier = Modifier.padding(top = 4.dp, start = 16.dp)
            )
        }
    }
}

/**
 * [OutlinedSupportingTextField]의 상태를 나타내는 클래스입니다.
 *
 * @property Correct 입력값이 기댓값과 동일한 경우를 나타냅니다.
 * @property Error 입력값이 기댓값과 다른 경우를 나타냅니다.
 * @property Empty 상태값이 필요없는 경우에 사용합니다.
 */
sealed class TextFieldState {
    data class Correct(val message: String) : TextFieldState()
    data class Error(val message: String) : TextFieldState()
    data object Empty : TextFieldState()

    fun getText(): String? = when (this) {
        is Correct -> message
        is Error -> message
        Empty -> null
    }

    @Composable
    fun getColor(): Color = when (this) {
        is Correct -> KuringTheme.colors.mainPrimary
        is Error -> KuringTheme.colors.warning
        Empty -> KuringTheme.colors.background
    }
}

@LightAndDarkPreview
@Composable
private fun OutlinedMessageTextField_Error_Preview() {
    KuringTheme {
        var query by remember { mutableStateOf("") }
        val supportingText = "등록되지 않은 이메일이에요."
        val placeholderText = "학교 이메일 주소"

        OutlinedSupportingTextField(
            query = query,
            onQueryUpdate = { query = it },
            placeholderText = placeholderText,
            textFieldState = Error(supportingText),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun OutlinedMessageTextField_Correct_Preview() {
    KuringTheme {
        var query by remember { mutableStateOf("") }
        val supportingText = "확인되었어요."
        val placeholderText = "인증번호"

        OutlinedSupportingTextField(
            query = query,
            onQueryUpdate = { query = it },
            placeholderText = placeholderText,
            textFieldState = Correct(supportingText),
            visualTransformation = PasswordVisualTransformation()
        )
    }
}