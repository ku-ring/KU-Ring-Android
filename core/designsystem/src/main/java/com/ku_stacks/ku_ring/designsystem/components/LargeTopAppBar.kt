package com.ku_stacks.ku_ring.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

/**
 * [CenterTitleTopBar]보다 더 넓은 공간을 차지하는 Top App Bar이다.
 *
 * @param title 제목 문자열
 * @param modifier 적용할 [Modifier]
 * @param navigationIconId Navigation 아이콘의 drawable ID
 * @param onNavigationIconClick Navigation 아이콘을 클릭했을 때 실행할 콜백
 * @param iconDescription Navigation 아이콘을 설명하는 문자열. 접근성 서비스에 제공되는 값이다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes navigationIconId: Int,
    onNavigationIconClick: () -> Unit,
    iconDescription: String? = null,
    action: @Composable () -> Unit = {},
) {
    LargeTopAppBar(
        title = {
            LargeTopAppBarTitle(title = title)
        },
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
                modifier = Modifier.padding(start = 14.dp, top = 14.dp)
            ) {
                Icon(
                    painter = painterResource(id = navigationIconId),
                    contentDescription = iconDescription,
                    tint = KuringTheme.colors.textBody,
                )
            }
        },
        actions = {
            action()
            Spacer(modifier = Modifier.width(14.dp))
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = KuringTheme.colors.background),
        modifier = modifier,
    )
}

@Composable
private fun LargeTopAppBarTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 36.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = KuringTheme.colors.textTitle,
        ),
        modifier = modifier.padding(start = 12.dp, top = 26.dp),
    )
}

@LightAndDarkPreview
@Composable
private fun LargeTopAppBarPreview() {
    KuringTheme {
        LargeTopAppBar(
            title = "학과를 추가하거나\n삭제할 수 있어요",
            modifier = Modifier.fillMaxWidth(),
            navigationIconId = R.drawable.ic_back_v2,
            onNavigationIconClick = {},
        ) {
            TextButton(onClick = { }) {
                Text(
                    text = "전체 삭제",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight(500),
                        color = KuringTheme.colors.mainPrimary,
                    )
                )
            }
        }
    }
}