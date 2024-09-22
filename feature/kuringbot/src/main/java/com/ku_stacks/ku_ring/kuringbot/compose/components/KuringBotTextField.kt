package com.ku_stacks.ku_ring.kuringbot.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.kuringbot.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun KuringBotTextField(
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    val textColor = KuringTheme.colors.textBody

    val textStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 24.45.sp,
        fontFamily = Pretendard,
        fontWeight = FontWeight(500),
        color = textColor,
    )

    val placeholderText = stringResource(id = R.string.kuringbot_text_field_placeholder)

    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = query,
        onValueChange = onQueryUpdate,
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = KuringTheme.colors.gray200, shape = shape),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                placeholder = {
                    Placeholder(
                        placeholderText = placeholderText,
                        style = textStyle.copy(color = KuringTheme.colors.textCaption3),
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = textColor,
                    backgroundColor = KuringTheme.colors.background,
                    cursorColor = textColor,
                ),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                interactionSource = interactionSource,
            )
        },
        singleLine = false,
        interactionSource = interactionSource,
        maxLines = 5,
        cursorBrush = SolidColor(textColor),
    )
}

@Composable
private fun Placeholder(
    placeholderText: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = placeholderText,
        modifier = modifier,
        style = style,
    )
}

@LightAndDarkPreview
@Composable
private fun SearchTextFieldPreview() {
    var query by remember { mutableStateOf("") }
    KuringTheme {
        KuringBotTextField(
            query = query,
            onQueryUpdate = { query = it },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}
