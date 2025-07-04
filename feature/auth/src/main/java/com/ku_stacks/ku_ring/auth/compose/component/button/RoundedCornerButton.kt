package com.ku_stacks.ku_ring.auth.compose.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

@Composable
internal fun RoundedCornerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(50),
    colors: ButtonColors = ButtonDefaults.textButtonColors(
        containerColor = KuringTheme.colors.mainPrimary,
        contentColor = KuringTheme.colors.background,
    ),
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = colors,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SignInButtonPreview() {
    KuringTheme {
        RoundedCornerButton(
            text = "로그인",
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}