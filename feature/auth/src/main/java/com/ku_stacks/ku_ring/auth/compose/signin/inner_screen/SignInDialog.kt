package com.ku_stacks.ku_ring.auth.compose.signin.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R.string.dialog_proceed_sign_in_wrong
import com.ku_stacks.ku_ring.feature.auth.R.string.dialog_sign_in_wrong

@Composable
internal fun SignInDialog(
    onDismissRequest: () -> Unit,
) {
    val backgroundColor = KuringTheme.colors.background
    val shape = RoundedCornerShape(15.dp)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 44.dp)
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = shape,
                )
        ) {
            Spacer(modifier = Modifier.height(43.dp))
            Text(
                text = stringResource(id = dialog_sign_in_wrong),
                style = TextStyle(
                    color = KuringTheme.colors.textBody,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None,
                    ),
                ),
            )
            Spacer(modifier = Modifier.height(30.dp))
            SignInConfirmButton(onClick = onDismissRequest)
        }
    }
}

@Composable
private fun SignInConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lineColor = KuringTheme.colors.borderline

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .drawBehind {
                drawLine(
                    color = lineColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        Text(
            text = stringResource(id = dialog_proceed_sign_in_wrong),
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.mainPrimary,
            ),
            modifier = Modifier
                .padding(top = 16.dp, bottom = 20.dp)
                .align(Alignment.Center),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun AuthDialogPreview() {
    KuringTheme {
        SignInDialog(onDismissRequest = {})
    }
}