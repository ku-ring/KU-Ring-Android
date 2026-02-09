package com.ku_stacks.ku_ring.main.club.compose.components


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.KuringScrollableTabRow
import com.ku_stacks.ku_ring.designsystem.components.KuringScrollableTabRowDefaults
import com.ku_stacks.ku_ring.designsystem.components.KuringScrollableTabRowDefaults.kuringTabIndicatorOffset
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.club.compose.type.ClubTabItem
import kotlinx.coroutines.launch

@Composable
internal fun ClubTabRow(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    
    KuringScrollableTabRow(
        selectedTabIndex = currentPage,
        backgroundColor = KuringTheme.colors.background,
        contentColor = KuringTheme.colors.mainPrimary,
        indicator = { tabPositions ->
            KuringScrollableTabRowDefaults.Indicator(
                modifier = Modifier
                    .kuringTabIndicatorOffset(tabPositions[currentPage])
                    .clip(RoundedCornerShape(50)),
                height = 4.dp,
            )
        },
        divider = {
            KuringScrollableTabRowDefaults.Divider(color = KuringTheme.colors.borderline)
        },
        edgePadding = 20.dp,
        modifier = modifier,
    ) {
        ClubTabItem.entries.forEachIndexed { index, clubTabItem ->
            ClubTab(
                tabItem = clubTabItem,
                isSelected = (index == currentPage),
                modifier = Modifier
                    .clickable(
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        role = Role.Tab,
                    )
                    .padding(
                        horizontal = 17.5.dp,
                        vertical = 12.5.dp,
                    ),
            )
        }
    }
}

@Composable
private fun ClubTab(
    tabItem: ClubTabItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) KuringTheme.colors.mainPrimary else KuringTheme.colors.textCaption1,
        label = "club tab text color",
    )
    Text(
        text = tabItem.koreanName,
        style = KuringTheme.typography.body2,
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@LightAndDarkPreview
@Composable
private fun ClubTabRowPreview() {
    KuringTheme {
        ClubTabRow(
            pagerState = rememberPagerState {
                ClubTabItem.entries.size
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )
    }
}
