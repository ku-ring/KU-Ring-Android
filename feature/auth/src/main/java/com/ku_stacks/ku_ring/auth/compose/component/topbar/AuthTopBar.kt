package com.ku_stacks.ku_ring.auth.compose.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.NavigateUpTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R

/**
 * Auth 모듈의 모든 화면에서 공통으로 사용되는 탑바 컴포넌트입니다.
 *
 * ColumnScope의 verticalArrangement를 사용하여 탑바와 하단 컨텐츠를 분리합니다.
 */
@Composable
internal fun AuthTopBar(
    headingText: String,
    subHeadingText: String,
    onBackButtonClick: () -> Unit,
) {
    NavigateUpTopBar(
        navigationIconId = R.drawable.ic_back_v2,
        onNavigationIconClick = onBackButtonClick,
        modifier = Modifier.fillMaxWidth(),
    )

    Column(
        modifier = Modifier.padding(start = 20.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = headingText,
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 36.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(700),
                color = KuringTheme.colors.textTitle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None,
                ),
            ),
            color = KuringTheme.colors.textBody,
        )

        Text(
            text = subHeadingText,
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                color = KuringTheme.colors.textCaption1,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None,
                ),
            ),
            color = KuringTheme.colors.textBody,
        )
    }
}

@LightAndDarkPreview
@Composable
private fun AuthTopBar_SignIn_Preview() {
    KuringTheme {
        Column(Modifier.background(KuringTheme.colors.background)) {
            AuthTopBar(
                headingText = "로그인",
                subHeadingText = "로그인 후 쿠링과 함께\n다채로운 캠퍼스 생활을 즐겨보세요:)",
                onBackButtonClick = {},
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun AuthTopBar_SignUp_Preview() {
    KuringTheme {
        Column(
            modifier = Modifier.background(KuringTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AuthTopBar(
                headingText = "개인정보 수집/이용 동의 ",
                subHeadingText = "회원 가입 전 하단 유의사항을 확인하고,\n개인정보 수집 및 이용에 동의해주세요.",
                onBackButtonClick = {},
            )
        }
    }
}
