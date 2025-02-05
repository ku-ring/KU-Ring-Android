package com.ku_stacks.ku_ring.designsystem.components.topbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

/**
 * 뒤로 가기 버튼만 포함하는 탑바.
 *
 * @param navigationIconId 아이콘 아이디
 * @param onNavigationIconClick 아이콘 버튼을 눌렀을 때 발생시킬 이벤트
 * @param modifier 적용할 [Modifier]
 */
@Composable
fun NavigateUpTopBar(
    @DrawableRes navigationIconId: Int,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .background(KuringTheme.colors.background)
            .padding(start = 20.dp, top = 18.dp, bottom = 21.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = navigationIconId),
            contentDescription = null,
            tint = KuringTheme.colors.textBody,
            modifier = Modifier.clickable { onNavigationIconClick() }
        )

    }
}

@LightAndDarkPreview
@Composable
private fun KuringIconTopBarPreview() {
    KuringTheme {
        NavigateUpTopBar(
            onNavigationIconClick = {},
            navigationIconId = R.drawable.ic_back_v2,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}