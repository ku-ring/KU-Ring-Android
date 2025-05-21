package com.ku_stacks.ku_ring.notice_detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.notice_detail.R

@Composable
internal fun CommentTextField(
    onCreateComment: (String) -> Unit,
    isReply: Boolean,
    modifier: Modifier = Modifier,
) {
    var comment by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CommentTextField(
            comment = comment,
            onCommentUpdate = { comment = it },
            isReply = isReply,
            modifier = Modifier.weight(1f),
        )
        CreateCommentButton(
            onClick = {
                onCreateComment(comment)
                comment = ""
            },
            enabled = comment.isNotEmpty(),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CommentTextField(
    comment: String,
    onCommentUpdate: (String) -> Unit,
    isReply: Boolean,
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

    val placeholderText = if (isReply) {
        stringResource(R.string.comment_bottom_sheet_textfield_reply_placeholder)
    } else {
        stringResource(R.string.comment_bottom_sheet_textfield_placeholder)
    }

    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = comment,
        onValueChange = onCommentUpdate,
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = KuringTheme.colors.gray200, shape = shape),
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = comment,
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


@Composable
private fun CreateCommentButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.comment_bottom_sheet_textfield_description)
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(KuringTheme.colors.gray400)
            .clickable(
                enabled = enabled,
                role = Role.Button,
                onClick = onClick,
            )
            .clearAndSetSemantics {
                contentDescription = description
            },
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_up_v2),
            contentDescription = null,
            modifier = Modifier.padding(8.dp),
            tint = KuringTheme.colors.background,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SearchTextFieldPreview() {
    KuringTheme {
        CommentTextField(
            onCreateComment = {},
            isReply = true,
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}