package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.R
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme

/**
 * [IconButton]과 같은 크기를 채우고 싶을 때 사용하는 Composable이다.
 *
 * @param modifier 적용할 [Modifier]. 단, 일반적으로 매개변수를 넘겨주지 않는 것이 권장되며,
 * UI 특성상 [Modifier]를 적용하더라도 크기와 관련된 것은 적용하지 않는 것을 권장한다.
 */
@Composable
fun TransparentIconButton(modifier: Modifier = Modifier) {
    IconButton(
        onClick = {},
        enabled = false,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            tint = Color.Transparent,
        )
    }
}

@LightPreview
@Composable
private fun TransparentIconButtonPreview() {
    KuringTheme {
        TransparentIconButton(
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
                .border(width = 1.dp, color = Color.White),
        )
    }
}