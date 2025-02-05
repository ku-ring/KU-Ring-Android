package com.ku_stacks.ku_ring.designsystem.components.indicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalSlidingIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    spacing: Dp = 8.dp,
) {
    // 두 indicator의 왼쪽 경계 간 거리
    val indicatorDistancePx = with(LocalDensity.current) {
        (dotSize + spacing).toPx()
    }

    val inactiveBackground = KuringTheme.colors.gray200
    val activeBackground = KuringTheme.colors.mainPrimary
    val dotShape = RoundedCornerShape(50)

    Box(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pagerState.pageCount) {
                Box(
                    modifier = Modifier
                        .background(inactiveBackground, shape = dotShape)
                        .size(dotSize),
                )
            }
        }
        Box(
            modifier = Modifier
                .horizontalSlidingTransition(pagerState, indicatorDistancePx)
                .size(dotSize)
                .background(activeBackground, shape = dotShape),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.horizontalSlidingTransition(pagerState: PagerState, distance: Float) =
    graphicsLayer {
        val pageOffset = pagerState.currentPage + pagerState.currentPageOffsetFraction
        translationX = pageOffset * distance
    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
@LightPreview
private fun HorizontalSlidingIndicatorPreview() {
    val pagerState = rememberPagerState(initialPage = 2) { 5 }
    KuringTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState,
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Gray)
                        .size(40.dp),
                ) {
                    Text(it.toString())
                }
            }
            Text("current page: ${pagerState.currentPage}")
            HorizontalSlidingIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .padding(16.dp),
            )
        }
    }
}