package com.ku_stacks.ku_ring.main.search.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.theme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.theme.values.Pretendard
import com.ku_stacks.ku_ring.main.R

@Composable
fun EmptyResultScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(KuringTheme.colors.background)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.search_advice),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textCaption2,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@LightAndDarkPreview
@Composable
private fun EmptyResultScreenPreview() {
    KuringTheme {
        EmptyResultScreen(modifier = Modifier.fillMaxSize())
    }
}