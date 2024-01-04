package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.CaptionGray1
import com.ku_stacks.ku_ring.designsystem.theme.CaptionGray2
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.Pretendard

/**
 * 제목과 내비게이션 아이콘, 액션 텍스트를 보여주는 최상단 바이다.
 * 액션을 텍스트로 표시하고자 할 때 사용하는 composable이다.
 *
 * @param title 표시할 제목 텍스트
 * @param modifier 적용할 [Modifier]
 * @param navigation 텍스트 왼쪽에 표시할 navigation indicator. 주로 뒤로 가기 아이콘이 사용되나, `취소` 등의 텍스트를 사용할 수도 있다.
 * @param onNavigationClick 내비게이션 컴포넌트를 클릭할 때 실행할 콜백.
 * @param navigationClickLabel 내비게이션 클릭 콜백을 설명하는 텍스트. 콜백이 null이 아니라면 접근성을 위해 제공하는 것이 좋다.
 * @param action 텍스트 오른쪽에 표시될 작업 텍스트. `완료`, `전송` 등의 텍스트가 사용될 수 있다.
 * @param onActionClick 액션 텍스트를 클릭할 때 실행할 콜백.
 * @param actionClickLabel 액션 클릭 콜백을 설명하는 텍스트. 콜백이 null이 아니라면 접근성을 위해 제공하는 것이 좋다.
 */
@Composable
fun CenterTitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigation: @Composable () -> Unit = {},
    onNavigationClick: (() -> Unit)? = null,
    navigationClickLabel: String? = null,
    action: String = "",
    onActionClick: (() -> Unit)? = null,
    actionClickLabel: String? = null,
) {
    CenterTitleTopBar(
        title = title,
        navigation = navigation,
        onNavigationClick = onNavigationClick,
        navigationClickLabel = navigationClickLabel,
        action = {
            Text(
                text = action,
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = MaterialTheme.colors.primary,
                ),
            )
        },
        onActionClick = onActionClick,
        actionClickLabel = actionClickLabel,
        modifier = modifier,
    )
}

/**
 * 제목과 내비게이션 아이콘, 액션을 보여주는 최상단 바이다.
 * 텍스트가 아닌 일반적인 composable을 액션으로 보여주고 싶을 때 사용하는 오버로딩이다.
 *
 * @param title 표시할 제목 텍스트
 * @param modifier 적용할 [Modifier]
 * @param navigation 텍스트 왼쪽에 표시할 navigation indicator. 주로 뒤로 가기 아이콘이 사용되나, `취소` 등의 텍스트를 사용할 수도 있다.
 * @param onNavigationClick 내비게이션 컴포넌트를 클릭할 때 실행할 콜백.
 * @param navigationClickLabel 내비게이션 클릭 콜백을 설명하는 텍스트. 콜백이 null이 아니라면 접근성을 위해 제공하는 것이 좋다.
 * @param action 텍스트 오른쪽에 표시될 작업. [Icon] 등을 넣을 수 있다.
 * @param onActionClick 액션 텍스트를 클릭할 때 실행할 콜백.
 * @param actionClickLabel 액션 클릭 콜백을 설명하는 텍스트. 콜백이 null이 아니라면 접근성을 위해 제공하는 것이 좋다.
 */
@Composable
fun CenterTitleTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigation: @Composable () -> Unit = {},
    onNavigationClick: (() -> Unit)? = null,
    navigationClickLabel: String? = null,
    action: @Composable () -> Unit = {},
    onActionClick: (() -> Unit)? = null,
    actionClickLabel: String? = null,
) {
    val backgroundColor = MaterialTheme.colors.surface
    val contentPadding = PaddingValues(13.dp)
    Box(
        modifier = modifier
            .background(backgroundColor)
            .padding(horizontal = 7.dp)
            .fillMaxWidth(),
    ) {
        Navigation(
            navigationIcon = navigation,
            onNavigationClick = onNavigationClick,
            navigationClickLabel = navigationClickLabel,
            contentPadding = contentPadding,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(50)),
        )
        TopBarTitle(
            title = title,
            backgroundColor = backgroundColor,
            modifier = Modifier.align(Alignment.Center),
        )
        Action(
            action = action,
            onActionClick = onActionClick,
            actionClickLabel = actionClickLabel,
            contentPadding = contentPadding,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(50)),
        )
    }
}

@Composable
private fun Action(
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onActionClick: (() -> Unit)? = null,
    actionClickLabel: String? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        modifier = modifier.clickable(
            onClick = { onActionClick?.invoke() },
            onClickLabel = actionClickLabel,
            enabled = onActionClick != null,
        ),
        contentPadding = contentPadding,
    ) {
        item {
            action()
        }
    }
}

@Composable
private fun TopBarTitle(
    title: String, backgroundColor: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = TextStyle(
            fontSize = 20.sp,
            lineHeight = 30.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = contentColorFor(backgroundColor = backgroundColor),
            textAlign = TextAlign.Center,
        ),
        modifier = modifier,
    )
}

@Composable
private fun Navigation(
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onNavigationClick: (() -> Unit)? = null,
    navigationClickLabel: String? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    CompositionLocalProvider(LocalContentColor provides CaptionGray1) {
        LazyColumn(
            modifier = modifier.clickable(
                onClick = { onNavigationClick?.invoke() },
                onClickLabel = navigationClickLabel,
                enabled = onNavigationClick != null,
            ),
            contentPadding = contentPadding,
        ) {
            item {
                navigationIcon()
            }
        }
    }
}

@LightAndDarkPreview
@Composable
private fun TopAppBarPreview_ActionText() {
    KuringTheme {
        CenterTitleTopBar(
            title = "푸시 알림 설정",
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                )
            },
            action = "완료",
            onActionClick = {},
            onNavigationClick = {},
        )
    }
}

@LightAndDarkPreview
@Composable
private fun TopAppBarPreview_ActionIcon() {
    KuringTheme {
        CenterTitleTopBar(
            title = "푸시 알림 설정",
            navigation = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                )
            },
            action = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_trashcan),
                    contentDescription = null,
                    tint = CaptionGray2,
                )
            },
            onActionClick = {},
        )
    }
}