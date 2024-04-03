package com.ku_stacks.ku_ring.main.setting.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R
import com.ku_stacks.ku_ring.main.setting.compose.components.OpenSourceItem

@Composable
internal fun OpenSourceListScreen(
    onNavigateToBack: () -> Unit,
    onNavigateToLottieFiles: () -> Unit,
    onNavigateToEmoji: () -> Unit,
    onNavigateToOssLicenses: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterTitleTopBar(
                title = stringResource(id = R.string.open_source_license),
                navigation = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_v2),
                        contentDescription = stringResource(id = R.string.open_source_back),
                        tint = KuringTheme.colors.gray600,
                    )
                },
                onNavigationClick = onNavigateToBack,
                action = {},
            )
        },
        modifier = modifier.background(KuringTheme.colors.background),
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        ) {
            OpenSourceTitle(modifier = Modifier.padding(start = 20.dp, top = 17.dp))
            OpenSourceItems(
                onNavigateToLottieFiles = onNavigateToLottieFiles,
                onNavigateToEmoji = onNavigateToEmoji,
                modifier = Modifier.padding(top = 56.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            OssLicensesText(
                onClick = onNavigateToOssLicenses,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 26.dp),
            )
        }
    }
}

@Composable
private fun OpenSourceTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.open_source_title),
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 34.08.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = KuringTheme.colors.textTitle,
        ),
        modifier = modifier,
    )
}

@Composable
private fun OpenSourceItems(
    onNavigateToLottieFiles: () -> Unit,
    onNavigateToEmoji: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OpenSourceItem(
            name = stringResource(id = R.string.open_source_item_lottie),
            onClick = onNavigateToLottieFiles,
        )
        OpenSourceItem(
            name = stringResource(id = R.string.open_source_item_emoji),
            onClick = onNavigateToEmoji,
        )
    }
}

@Composable
private fun OssLicensesText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = R.string.open_source_oss_licenses),
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 22.82.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
        ),
        modifier = modifier.clickable(onClick = onClick),
    )
}

@LightAndDarkPreview
@Composable
private fun OpenSourcesListScreenPreview() {
    KuringTheme {
        OpenSourceListScreen(
            onNavigateToBack = {},
            onNavigateToLottieFiles = {},
            onNavigateToEmoji = {},
            onNavigateToOssLicenses = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}