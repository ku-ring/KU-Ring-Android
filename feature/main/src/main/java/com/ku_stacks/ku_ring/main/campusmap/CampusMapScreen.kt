package com.ku_stacks.ku_ring.main.campusmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.components.topbar.CenterTitleTopBar
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R

@Composable
fun CampusMapScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            CenterTitleTopBar(
                title = stringResource(R.string.campus_map_title),
                navigation = {},
                action = {},
                modifier = Modifier.padding(16.dp),
            )
        },
        modifier = modifier.fillMaxSize(),
        backgroundColor = KuringTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_construction),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.campus_on_construction),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 22.82.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = KuringTheme.colors.textCaption1,
                )
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun CampusMapScreenPreview() {
    KuringTheme {
        CampusMapScreen()
    }
}