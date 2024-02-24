package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.Gray2
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard

@Composable
fun KuringAlertDialog(
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(id = R.string.confirm),
    dismissText: String = stringResource(id = R.string.cancel),
    confirmTextColor: Color = MaterialTheme.colors.primary,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        KuringAlertDialogContents(
            text = text,
            confirmText = confirmText,
            onConfirm = onConfirm,
            dismissText = dismissText,
            onDismiss = onDismiss,
            modifier = modifier,
            confirmTextColor = confirmTextColor,
        )
    }
}

@Composable
private fun KuringAlertDialogContents(
    text: String,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmTextColor: Color = MaterialTheme.colors.primary,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
            .size(width = 314.dp, height = 186.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 버튼 클릭 이펙트는 위아래로 16dp씩 보이면서
        // 텍스트의 위치는 팝업 상단과 버튼 텍스트와 중간을 맞추기 위해
        // KuringAlertDialogText에 top padding을 16dp 줌
        Box(modifier = Modifier.weight(1f)) {
            KuringAlertDialogText(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 16.dp),
            )
        }
        KuringAlertDialogButtons(
            confirmText = confirmText,
            onConfirm = onConfirm,
            dismissText = dismissText,
            onDismiss = onDismiss,
            confirmTextColor = confirmTextColor,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun KuringAlertDialogText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 27.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
        ),
        modifier = modifier,
    )
}

@Composable
private fun KuringAlertDialogButtons(
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmTextColor: Color = Gray2,
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        KuringAlertDialogButton(
            text = dismissText,
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
        )
        Divider(
            color = Color(0x26000000),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxHeight()
                .width(1.dp),
        )
        KuringAlertDialogButton(
            text = confirmText,
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
            textColor = confirmTextColor,
        )
    }
}

@Composable
private fun KuringAlertDialogButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Gray2,
) {
    Box(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = textColor,
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 20.dp),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun KuringAlertDialogPreview() {
    KuringTheme {
        KuringAlertDialog(
            text = "스마트ICT융합공학과를\n내 학과 목록에 추가할까요?",
            onConfirm = {},
            onDismiss = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}