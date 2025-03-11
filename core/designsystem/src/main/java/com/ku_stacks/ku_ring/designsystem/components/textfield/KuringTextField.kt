package com.ku_stacks.ku_ring.designsystem.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

/**
 * Kuring의 기본 TextField 컴포넌트로, 앞뒤로 아이콘 및 컴포넌트 부착이 가능하다.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuringTextField(
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = KuringTextFieldDefault.textStyle(),
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = KuringTextFieldDefault.colors(),
    placeholderText: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentPadding: PaddingValues = PaddingValues(all = 0.dp),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryUpdate,
        modifier = modifier.defaultMinSize(minHeight = KuringTextFieldDefault.minHeight),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                shape = shape,
                singleLine = singleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    Placeholder(
                        placeholderText = placeholderText,
                        textStyle = textStyle.copy(color = KuringTheme.colors.textCaption1),
                    )
                },
                suffix = suffix,
                prefix = prefix,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                colors = colors,
                contentPadding = contentPadding,
            )
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
    )
}

@Composable
private fun Placeholder(
    placeholderText: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = placeholderText,
        modifier = modifier,
        style = textStyle,
    )
}

object KuringTextFieldDefault {

    /**
     * KuringTextField의 기본 텍스트스타일을 포함하는 TextStyle 객체를 반환합니다.
     *
     * @param fontSize 텍스트의 크기
     * @param lineHeight 텍스트의 줄간격
     * @param fontFamily 텍스트의 폰트
     * @param fontWeight 텍스트의 굵기
     * @param color 텍스트의 색상
     *
     * @return [TextStyle]
     */
    @Composable
    fun textStyle(
        fontSize: TextUnit = defaultFontSize,
        lineHeight: TextUnit = defaultLineHeight,
        fontFamily: FontFamily = defaultFontFamily,
        fontWeight: FontWeight = defaultFontWeight,
        color: Color = defaultTextColor,
    ): TextStyle = TextStyle(
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        color = color,
    )

    /**
     * KuringTextField의 기본 색상을 포함하는 [TextFieldColors] 객체를 반환합니다.
     *
     * @param textColor 텍스트 색상
     * @param backgroundColor 배경색
     * @param cursorColor 커서 색상
     * @param indicatorColor 인디케이터 색상
     *
     * @return [TextFieldColors]
     */
    @Composable
    fun colors(
        textColor: Color = defaultTextColor,
        backgroundColor: Color = defaultBackgroundColor,
        cursorColor: Color = defaultCursorColor,
        indicatorColor: Color = Color.Transparent,
    ): TextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = textColor,
        unfocusedTextColor = textColor,
        focusedContainerColor = backgroundColor,
        unfocusedContainerColor = backgroundColor,
        focusedIndicatorColor = indicatorColor,
        unfocusedIndicatorColor = indicatorColor,
        cursorColor = cursorColor,
    )

    private val defaultFontSize: TextUnit = 16.sp
    private val defaultLineHeight: TextUnit = 24.sp
    private val defaultFontFamily: FontFamily = Pretendard
    private val defaultFontWeight: FontWeight = FontWeight.Medium
    private val defaultTextColor: Color
        @Composable get() = KuringTheme.colors.textBody
    private val defaultBackgroundColor: Color
        @Composable get() = KuringTheme.colors.gray100
    private val defaultCursorColor: Color
        @Composable get() = KuringTheme.colors.textBody


    val minHeight: Dp = 50.dp
}

@LightAndDarkPreview
@Composable
private fun KuringTextFieldPreview() {
    var query by remember { mutableStateOf("") }
    KuringTheme {
        val shape = RoundedCornerShape(50)

        KuringTextField(
            shape = shape,
            query = query,
            onQueryUpdate = { query = it },
            placeholderText = "추가할 학과를 검색해 주세요",
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(16.dp)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_v2),
                    contentDescription = null,
                    tint = KuringTheme.colors.textCaption1,
                )
            }
        )
    }
}
