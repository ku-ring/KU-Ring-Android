package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

/**
 * 쿠링 앱에서 사용될 CTA 버튼이다.
 * 화면 하단에 위치하며, 사용자가 수행할 메인 작업을 표현한다.
 *
 * @param text CTA에 보여줄 텍스트
 * @param onClick CTA를 클릭했을 때 실행할 콜백
 * @param modifier CTA에 적용될 [Modifier]
 * @param enabled CTA 버튼이 활성화되었는지를 나타낸다. true라면 배경색이 [KuringTheme.colors.mainPrimary]로 설정되며,
 * false라면 배경색이 [KuringTheme.colors.gray200]로 설정된다. 컨텐츠 색깔은 둘 중 배경색으로 사용되지 않은 나머지 색으로 설정된다.
 * @param blur 버튼 위에 블러 효과를 적용할 지 결정한다. 블러는 버튼 위에 버튼 높이의 1/4만큼 그려진다.
 */
@Composable
fun KuringCallToAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    blur: Boolean = false,
) {
    KuringCallToAction(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        blur = blur,
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(600),
            ),
        )
    }
}

/**
 * 쿠링 앱에서 사용될 CTA 버튼이다.
 * 화면 하단에 위치하며, 사용자가 수행할 메인 작업을 표현한다.
 *
 * @param onClick CTA를 클릭했을 때 실행할 콜백
 * @param modifier CTA에 적용될 [Modifier]
 * @param enabled CTA 버튼이 활성화되었는지를 나타낸다. true라면 배경색이 [KuringTheme.colors.mainPrimary]로 설정되며,
 * false라면 배경색이 [KuringTheme.colors.gray200]로 설정된다. 컨텐츠 색깔은 둘 중 배경색으로 사용되지 않은 나머지 색으로 설정된다.
 * @param blur 버튼 위에 블러 효과를 적용할 지 결정한다. 블러는 버튼 위에 버튼 높이의 1/4만큼 그려진다.
 */
@Composable
fun KuringCallToAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    blur: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(horizontal = 50.dp, vertical = 16.dp),
    contents: @Composable () -> Unit,
) {
    KuringCallToActionBase(
        onClick = onClick,
        paddingValues = contentPadding,
        enabled = enabled,
        blur = blur,
        modifier = modifier,
        contents = contents,
    )
}

@Composable
private fun KuringCallToActionBase(
    onClick: () -> Unit,
    paddingValues: PaddingValues,
    enabled: Boolean,
    blur: Boolean,
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) KuringTheme.colors.mainPrimary else KuringTheme.colors.gray200,
        label = "background color",
    )
    val contentColor by animateColorAsState(
        targetValue = if (enabled) KuringTheme.colors.background else KuringTheme.colors.textCaption1,
        label = "content color",
    )

    val surfaceColor = KuringTheme.colors.background
    val blurModifier = Modifier.drawBehind {
        if (blur) {
            val gradientHeight = size.height * 0.3f
            val gradientBrush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, surfaceColor),
                startY = -gradientHeight,
                endY = 0f,
            )
            drawRect(
                brush = gradientBrush,
                topLeft = Offset(x = 0f, y = -gradientHeight),
                size = Size(width = size.width, height = gradientHeight),
            )
        }
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(50),
        elevation = null,
        contentPadding = paddingValues,
        enabled = enabled,
        modifier = modifier
            .then(blurModifier)
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        contents()
    }
}

@LightAndDarkPreview
@Composable
private fun KuringCallToActionPreview_Enabled() {
    KuringTheme {
        KuringCallToAction(
            text = "완료",
            enabled = true,
            onClick = { },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun KuringCallToActionPreview_Enabled_Blur() {
    KuringTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(top = 30.dp),
        ) {
            KuringCallToAction(
                text = "완료",
                enabled = true,
                blur = true,
                onClick = { },
                modifier = Modifier
                    .background(KuringTheme.colors.background)
                    .fillMaxWidth(),
            )
        }
    }
}