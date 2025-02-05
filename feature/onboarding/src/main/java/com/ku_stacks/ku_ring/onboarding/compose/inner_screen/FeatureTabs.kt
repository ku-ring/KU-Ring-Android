package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.indicator.HorizontalSlidingIndicator
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.onboarding.R
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.feature_tab.FeatureTab
import com.ku_stacks.ku_ring.onboarding.compose.inner_screen.feature_tab.FeatureTabItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FeatureTabs(
    onNavigateToSetDepartment: () -> Unit,
    onSkipOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabItems = FeatureTabItem.values()
    val pagerState = rememberPagerState { tabItems.size }
    Column(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .fillMaxSize(),
    ) {
        FeatureTabsTitle(
            modifier = Modifier.padding(top = 76.dp, start = 20.dp),
        )
        FeatureTabItems(
            tabItems = tabItems,
            pagerState = pagerState,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.weight(1f))
        HorizontalSlidingIndicator(
            pagerState = pagerState,
            dotSize = 8.dp,
            spacing = 8.dp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        KuringCallToAction(
            text = stringResource(id = R.string.go_to_department_set),
            onClick = onNavigateToSetDepartment,
            enabled = pagerState.currentPage == tabItems.lastIndex,
            modifier = Modifier.fillMaxWidth(),
        )
        SkipOnboarding(
            onSkip = onSkipOnboarding,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 26.dp),
        )
    }
}

@Composable
private fun FeatureTabsTitle(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.tab_screen_title),
        modifier = modifier,
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 34.08.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = KuringTheme.colors.textTitle,
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FeatureTabItems(
    tabItems: Array<FeatureTabItem>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) {
        FeatureTab(
            item = tabItems[it],
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun SkipOnboarding(
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.tab_screen_skip_onboarding),
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 26.08.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
        ),
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable(onClick = onSkip, role = Role.Button)
            .padding(8.dp),
    )
}

@LightAndDarkPreview
@Composable
private fun FeatureTabsPreview() {
    KuringTheme {
        FeatureTabs(
            onNavigateToSetDepartment = {},
            onSkipOnboarding = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}