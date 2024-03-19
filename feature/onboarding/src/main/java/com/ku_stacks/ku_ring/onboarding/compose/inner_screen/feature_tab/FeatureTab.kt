package com.ku_stacks.ku_ring.onboarding.compose.inner_screen.feature_tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

@Composable
internal fun FeatureTab(
    item: FeatureTabItem,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = item.imageId),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 46.dp, vertical = 51.dp),
        )
        Text(
            text = stringResource(id = item.stringId),
            style = TextStyle(
                fontSize = 18.sp,
                lineHeight = 26.64.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(600),
                color = KuringTheme.colors.textBody,
                textAlign = TextAlign.Center,
            ),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun FeatureTabPreview() {
    KuringTheme {
        FeatureTab(
            item = FeatureTabItem.BELL,
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxWidth(),
        )
    }
}