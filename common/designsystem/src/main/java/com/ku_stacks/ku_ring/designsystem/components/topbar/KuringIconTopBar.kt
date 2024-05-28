package com.ku_stacks.ku_ring.designsystem.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme

/**
 * 왼쪽에 쿠링 아이콘을 보여주는 top bar이다.
 * Top bar 오른쪽 끝에 아이콘 등을 보여줄 수 있다.
 *
 * @param modifier 적용할 [Modifier]
 * @param content 오른쪽 끝에 보여줄 composable
 */
@Composable
fun KuringIconTopBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {},
) {
    Row(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .padding(vertical = 12.dp)
            .padding(start = 20.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_kuring_v2),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.weight(1f))
        content()
    }
}

@LightAndDarkPreview
@Composable
private fun KuringIconTopBarPreview() {
    KuringTheme {
        KuringIconTopBar(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_v2),
                contentDescription = null,
                tint = KuringTheme.colors.gray600,
            )
        }
    }
}