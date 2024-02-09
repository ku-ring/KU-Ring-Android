package com.ku_stacks.ku_ring.main.search.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.BoxBackgroundColor2
import com.ku_stacks.ku_ring.designsystem.theme.CaptionGray1
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.main.R

@Composable
fun SearchTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onClickClearButton: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentPadding: PaddingValues = PaddingValues(all = 0.dp),
) {

    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.surface,
        disabledTextColor = Color.Transparent,
        backgroundColor = BoxBackgroundColor2,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 0.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(color = colors.backgroundColor(true).value)
    ) {

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .padding(start = 16.dp, top = 10.dp, bottom = 10.dp, end = 8.dp)
                .height(20.dp)
                .width(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                tint = CaptionGray1,
            )
        }

        @OptIn(ExperimentalMaterialApi::class)
        BasicTextField(
            value = value,
            modifier = Modifier
                .weight(1f)
                .background(colors.backgroundColor(true).value)
                .align(Alignment.CenterVertically),
            onValueChange = onValueChange,
            textStyle = textStyle,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            singleLine = true,
            maxLines = 1,
            minLines = 1,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value.text,
                    visualTransformation = visualTransformation,
                    innerTextField = innerTextField,
                    placeholder = { PlaceholderText() },
                    singleLine = true,
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = colors,
                    contentPadding = contentPadding,
                )
            },
        )

        if (value.text.isNotEmpty()) {
            ClearButton(
                modifier = Modifier
                    .padding(start = 8.dp, top = 10.dp, bottom = 10.dp, end = 16.dp)
                    .height(20.dp)
                    .width(20.dp)
                    .clickable { onClickClearButton() }
            )
        }
    }
}

@Composable
private fun PlaceholderText() {
    Text(
        text = stringResource(id = R.string.search_enter_keyword),
        modifier = Modifier,
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(400),
            color = Color(0xFF8E8E8E),
        )
    )
}

@Composable
private fun ClearButton(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = CaptionGray1,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField(
        value = TextFieldValue("산학협력관"),
        onValueChange = {},
        onClickClearButton = {},
        modifier = Modifier.width(260.dp),
    )
}
