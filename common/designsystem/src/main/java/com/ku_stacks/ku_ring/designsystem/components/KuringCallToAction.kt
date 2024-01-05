package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.theme.KuringSub
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard

/**
 * 쿠링 앱에서 사용될 CTA 버튼이다.
 * 주로 화면 하단에 위치하며, 사용자가 수행할 메인 작업을 표현한다.
 *
 * @param text CTA에 보여줄 텍스트
 * @param onClick CTA를 클릭했을 때 실행할 콜백
 * @param modifier CTA에 적용될 [Modifier]
 * @param enabled CTA 버튼이 활성화되었는지를 나타낸다. true라면 배경색이 [MaterialTheme.colors.primary]로 설정되며,
 * false라면 배경색이 [KuringSub]로 설정된다. 컨텐츠 색깔은 둘 중 배경색으로 사용되지 않은 나머지 색으로 설정된다.
 */
@Composable
fun KuringCallToAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    KuringCallToAction(onClick = onClick, enabled = enabled, modifier = modifier) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 26.08.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(600),
            ),
        )
    }
}

/**
 * 쿠링 앱에서 사용될 CTA 버튼이다.
 * 주로 화면 하단에 위치하며, 사용자가 수행할 메인 작업을 표현한다.
 *
 * @param onClick CTA를 클릭했을 때 실행할 콜백
 * @param modifier CTA에 적용될 [Modifier]
 * @param enabled CTA 버튼이 활성화되었는지를 나타낸다. true라면 배경색이 [MaterialTheme.colors.primary]로 설정되며,
 * false라면 배경색이 [KuringSub]로 설정된다. 컨텐츠 색깔은 둘 중 배경색으로 사용되지 않은 나머지 색으로 설정된다.
 */
@Composable
fun KuringCallToAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contents: @Composable () -> Unit,
) {
    KuringCallToActionBase(
        onClick = onClick,
        paddingValues = PaddingValues(horizontal = 50.dp, vertical = 16.dp),
        enabled = enabled,
        modifier = modifier,
        contents = contents,
    )
}

@Composable
private fun KuringCallToActionBase(
    onClick: () -> Unit,
    paddingValues: PaddingValues,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) MaterialTheme.colors.primary else KuringSub,
        label = "background color",
    )
    val contentColor by animateColorAsState(
        targetValue = if (enabled) KuringSub else MaterialTheme.colors.primary,
        label = "content color",
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(50),
        elevation = null,
        contentPadding = paddingValues,
        modifier = modifier,
    ) {
        contents()
    }
}

@LightAndDarkPreview
@Composable
private fun KuringCallToActionPreview() {
    var isEnabled by remember { mutableStateOf(false) }

    KuringTheme {
        KuringCallToAction(
            text = "완료",
            enabled = isEnabled,
            onClick = { isEnabled = !isEnabled },
            modifier = Modifier
                .background(Color.Gray)
                .padding(16.dp)
                .fillMaxWidth(),
        )
    }
}