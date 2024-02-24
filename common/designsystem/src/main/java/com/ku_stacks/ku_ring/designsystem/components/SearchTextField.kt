package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.BoxBackgroundColor2
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard
import com.ku_stacks.ku_ring.designsystem.theme.TextCaption1

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchTextField(
    query: String,
    onQueryUpdate: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "",
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val shape = RoundedCornerShape(50)
    val backgroundColor = BoxBackgroundColor2
    val textColor = contentColorFor(backgroundColor)

    BasicTextField(
        value = query,
        onValueChange = onQueryUpdate,
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape = shape)
            .border(1.dp, MaterialTheme.colors.primary, shape = shape),
        textStyle = TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(400),
        ),
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = singleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    Placeholder(placeholderText = placeholderText)
                },
                leadingIcon = {
                    LeadingSearchIcon()
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = query.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        TrailingDeleteIcon(onClear = { onQueryUpdate("") })
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = textColor,
                    backgroundColor = backgroundColor,
                ),
            )
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
    )
}

@Composable
private fun LeadingSearchIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.ic_search),
        contentDescription = null,
        tint = MaterialTheme.colors.primary,
        modifier = modifier,
    )
}

@Composable
private fun Placeholder(
    placeholderText: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = placeholderText,
        modifier = modifier,
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
private fun TrailingDeleteIcon(
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_close),
        contentDescription = null,
        tint = TextCaption1,
        modifier = modifier
            .scale(0.8f)
            .clickable(onClick = onClear),
    )
}

@LightAndDarkPreview
@Composable
private fun SearchTextFieldPreview() {
    var query by remember { mutableStateOf("") }
    KuringTheme {
        SearchTextField(
            query = query,
            onQueryUpdate = { query = it },
            placeholderText = "추가할 학과를 검색해 주세요",
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}