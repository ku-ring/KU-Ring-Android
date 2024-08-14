package com.ku_stacks.ku_ring.main.notice.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R

@Composable
internal fun KuringBotFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        backgroundColor = KuringTheme.colors.mainPrimary,
        contentColor = Color.White,
        modifier = modifier.size(64.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_app_v2),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White,
            )
            Text(
                text = stringResource(id = R.string.main_screen_fab),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 16.3.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                )
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun KuringBotFabPreview() {
    KuringTheme {
        KuringBotFab(onClick = { })
    }
}