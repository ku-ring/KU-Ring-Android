package com.ku_stacks.ku_ring.designsystem.components.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuringIconTopBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = KuringTheme.colors.background),
        title = {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(start = 20.dp, end = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_kuring_v2),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.weight(1f))
                content()
            }
        }
    )
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