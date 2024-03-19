package com.ku_stacks.ku_ring.splash.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.splash.R

@Composable
internal fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_app),
            contentDescription = stringResource(id = R.string.splash_app_icon),
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@LightAndDarkPreview
@Composable
private fun SplashScreenPreview() {
    KuringTheme {
        SplashScreen(
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}