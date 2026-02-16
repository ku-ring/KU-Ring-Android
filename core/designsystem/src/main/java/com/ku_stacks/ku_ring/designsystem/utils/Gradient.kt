package com.ku_stacks.ku_ring.designsystem.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 컴포넌트의 경계 외부로 뻗어나가는 그라데이션 오버레이를 그립니다.
 *
 * 주로 리스트 아이템이 특정 버튼(예: 확장 버튼) 아래로 스며들며 페이드 아웃되는 효과를
 * 구현할 때 사용합니다.
 *
 * **주의사항:**
 * 1. 시각적으로는 경계 밖으로 그려지지만, 터치 이벤트와 레이아웃 크기에는 영향을 주지 않습니다.
 * 2. 부모 레이아웃에 의해 그라데이션이 잘릴 수 있습니다.
 * 3. 각 가장자리에 독립적인 직사각형을 그리는 방식이므로,
 * 모서리에서 이어지는 대각선 방향의 그라데이션은 지원하지 않습니다.
 *
 * @param colors 그라데이션에 사용할 색상 리스트
 * @param start 왼쪽 경계 밖으로 뻗어나갈 두께
 * @param top 위쪽 경계 밖으로 뻗어나갈 두께
 * @param end 오른쪽 경계 밖으로 뻗어나갈 두께
 * @param bottom 아래쪽 경계 밖으로 뻗어나갈 두께
 */
fun Modifier.overlayingGradient(
    colors: List<Color>,
    start: Dp = 0.dp,
    top: Dp = 0.dp,
    end: Dp = 0.dp,
    bottom: Dp = 0.dp,
) = this
    .graphicsLayer(clip = false)
    .then(GradientElement(colors, start, top, end, bottom))

private data class GradientElement(
    val colors: List<Color>,
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
) : ModifierNodeElement<GradientNode>() {

    override fun create() = GradientNode(colors, start, top, end, bottom)

    override fun update(node: GradientNode) {
        node.colors = colors
        node.start = start
        node.top = top
        node.end = end
        node.bottom = bottom
        node.invalidateDraw()
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "overlayingGradient"
        properties["colors"] = colors
        properties["start"] = start
        properties["top"] = top
        properties["end"] = end
        properties["bottom"] = bottom
    }
}

private class GradientNode(
    var colors: List<Color>,
    var start: Dp,
    var top: Dp,
    var end: Dp,
    var bottom: Dp
) : DrawModifierNode, Modifier.Node() {

    override fun ContentDrawScope.draw() {
        drawContent()

        if (start > 0.dp) {
            val width = start.toPx()
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = colors,
                    startX = -width,
                    endX = 0f,
                ),
                topLeft = Offset(x = -width, y = 0f),
                size = size.copy(width = width),
            )
        }

        if (end > 0.dp) {
            val width = end.toPx()
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = colors,
                    startX = size.width + width,
                    endX = size.width,
                ),
                topLeft = Offset(x = size.width, y = 0f),
                size = size.copy(width = width),
            )
        }

        if (top > 0.dp) {
            val height = top.toPx()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = colors,
                    startY = -height,
                    endY = 0f
                ),
                topLeft = Offset(x = 0f, y = -height),
                size = size.copy(height = height),
            )
        }

        if (bottom > 0.dp) {
            val height = bottom.toPx()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = colors,
                    startY = size.height + height,
                    endY = size.height,
                ),
                topLeft = Offset(x = 0f, y = size.height),
                size = size.copy(height = height)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GradientPreview() {
    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        val color = Color.Black
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(color)
                .overlayingGradient(
                    colors = with(color) {
                        listOf(
                            copy(alpha = 0f),
                            copy(alpha = 0.2f),
                            copy(alpha = 0.5f),
                            copy(alpha = 1f),
                        )
                    },
                    start = 50.dp,
                    top = 50.dp,
                    end = 50.dp,
                    bottom = 50.dp,
                )
        )
    }
}
