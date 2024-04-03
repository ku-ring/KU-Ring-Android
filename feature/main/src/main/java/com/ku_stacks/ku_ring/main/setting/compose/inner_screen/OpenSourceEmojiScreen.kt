package com.ku_stacks.ku_ring.main.setting.compose.inner_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@Composable
internal fun OpenSourceEmojiScreen(
    navigateToBack: () -> Unit,
    navigateToCopyright: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterTitleTopBar(
                title = stringResource(id = R.string.open_source_emoji_top_bar),
                navigation = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_v2),
                        contentDescription = stringResource(id = R.string.open_source_back),
                        tint = KuringTheme.colors.gray600,
                    )
                },
                onNavigationClick = navigateToBack,
                action = {},
            )
        },
        modifier = modifier.background(KuringTheme.colors.background),
    ) {
        Column(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(it)
                .fillMaxSize(),
        ) {
            EmojiTitle(modifier = Modifier.padding(start = 20.dp, top = 12.dp))
            EmojiContents(
                navigateToCopyright = navigateToCopyright,
                modifier = Modifier.padding(start = 20.dp, top = 56.dp),
            )
            EmojiDetails(modifier = Modifier.padding(start = 20.dp, top = 24.dp))
        }
    }
}

@Composable
private fun EmojiTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.open_source_emoji_title),
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
private fun EmojiContents(
    navigateToCopyright: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = stringResource(id = R.string.open_source_emoji_explanation),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.45.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textBody,
            )
        )
        TossFaceCopyrightContent(navigateToCopyright = navigateToCopyright)
    }
}

@Composable
private fun TossFaceCopyrightContent(
    navigateToCopyright: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable(onClick = navigateToCopyright),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.open_source_emoji_toss_face),
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 22.82.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(400),
                color = KuringTheme.colors.textCaption1,
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_external_link_v2),
            contentDescription = null,
            tint = KuringTheme.colors.gray300,
        )
    }
}

@Composable
internal fun EmojiDetails(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        EmojiDetailText(text = stringResource(id = R.string.open_source_emoji_detail1))
        EmojiDetailText(text = stringResource(id = R.string.open_source_emoji_detail2))
        EmojiDetailText(text = stringResource(id = R.string.open_source_emoji_detail3))
    }
}

@Composable
private fun EmojiDetailText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 15.sp,
            lineHeight = 24.45.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textBody,
        ),
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun OpenSourcesTossFaceScreenPreview() {
    KuringTheme {
        OpenSourceEmojiScreen(
            navigateToBack = {},
            navigateToCopyright = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}