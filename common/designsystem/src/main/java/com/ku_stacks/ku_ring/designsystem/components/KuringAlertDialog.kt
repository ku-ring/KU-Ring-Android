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
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

/**
 * 사용자에게 특정 행동의 수행 여부를 물어보는 [Dialog]이다.
 *
 * 작업을 취소할 때와 팝업을 무시할 때의 행동이 **같을 때** 사용하는 오버로딩이다.
 *
 * @param text 사용자에게 보여줄 메인 테스트
 * @param onConfirm 사용자가 작업을 수행하기로 결정했을 때 호출할 콜백
 * @param onCancel 사용자가 작업을 취소하거나 팝업을 무시할 때 호출할 콜백
 * @param modifier 적용할 [Modifier]
 * @param confirmText 하단 오른쪽의 작업 수행 버튼에 보여줄 텍스트
 * @param cancelText 하단 왼쪽의 작업 취소 버튼에 보여줄 텍스트
 * @param confirmTextColor 작업 수행 버튼에 적용할 텍스트 색깔
 */
@Composable
fun KuringAlertDialog(
    text: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(id = R.string.confirm),
    cancelText: String = stringResource(id = R.string.cancel),
    confirmTextColor: Color = KuringTheme.colors.mainPrimary,
) {
    KuringAlertDialog(
        text = text,
        onConfirm = onConfirm,
        onCancel = onCancel,
        onDismiss = onCancel,
        modifier = modifier,
        confirmText = confirmText,
        cancelText = cancelText,
        confirmTextColor = confirmTextColor,
    )
}

/**
 * 사용자에게 특정 행동의 수행 여부를 물어보는 [Dialog]이다.
 *
 * 작업을 취소할 때와 팝업을 무시할 때의 행동이 **다를 때** 사용하는 오버로딩이다.
 *
 * @param text 사용자에게 보여줄 메인 테스트
 * @param onConfirm 사용자가 작업을 수행하기로 결정했을 때 호출할 콜백
 * @param onCancel 사용자가 작업을 취소할 때 호출할 콜백
 * @param onDismiss 사용자가 팝업을 무시할 때 호출할 콜백
 * @param modifier 적용할 [Modifier]
 * @param confirmText 하단 오른쪽의 작업 수행 버튼에 보여줄 텍스트
 * @param cancelText 하단 왼쪽의 작업 취소 버튼에 보여줄 텍스트
 * @param confirmTextColor 작업 수행 버튼에 적용할 텍스트 색깔
 */
@Composable
fun KuringAlertDialog(
    text: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = stringResource(id = R.string.confirm),
    cancelText: String = stringResource(id = R.string.cancel),
    confirmTextColor: Color = KuringTheme.colors.mainPrimary,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        KuringAlertDialogContents(
            text = text,
            confirmText = confirmText,
            onConfirm = onConfirm,
            cancelText = cancelText,
            onCancel = onCancel,
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
    cancelText: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    confirmTextColor: Color = KuringTheme.colors.mainPrimary,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(KuringTheme.colors.background)
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
            cancelText = cancelText,
            onCancel = onCancel,
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
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textBody,
            textAlign = TextAlign.Center,
        ),
        modifier = modifier,
    )
}

@Composable
private fun KuringAlertDialogButtons(
    confirmText: String,
    onConfirm: () -> Unit,
    cancelText: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    confirmTextColor: Color = KuringTheme.colors.mainPrimary,
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        KuringAlertDialogButton(
            text = cancelText,
            onClick = onCancel,
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
    textColor: Color = KuringTheme.colors.textCaption1,
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
            onCancel = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}