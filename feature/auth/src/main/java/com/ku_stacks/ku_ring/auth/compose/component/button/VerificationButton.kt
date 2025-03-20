package com.ku_stacks.ku_ring.auth.compose.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

@Composable
internal fun VerificationButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(8.dp)
    val contentColor =
        if (enabled) KuringTheme.colors.mainPrimary
        else KuringTheme.colors.gray300
    val containerColor =
        if (enabled) KuringTheme.colors.mainPrimarySelected
        else KuringTheme.colors.gray100

    Button (
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        shape = shape,
        border = BorderStroke(width = 1.dp, color = contentColor),
        modifier = modifier.defaultMinSize(minHeight = buttonMinHeight),
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

private val buttonMinHeight = 50.dp

@LightAndDarkPreview
@Composable
private fun VerificationInputButtonPreview() {
    Column {
        VerificationButton(
            text = "인증하기",
            onClick = {},
            enabled = true,
        )
        VerificationButton(
            text = "인증하기",
            onClick = {},
            enabled = false,
        )
    }
}
