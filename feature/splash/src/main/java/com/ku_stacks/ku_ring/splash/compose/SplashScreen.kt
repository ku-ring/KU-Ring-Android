package com.ku_stacks.ku_ring.splash.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.splash.R
import com.ku_stacks.ku_ring.splash.SplashScreenState
import com.ku_stacks.ku_ring.splash.compose.components.UpdateAppDialog

@Composable
fun SplashScreen(
    screenState: SplashScreenState,
    onUpdateApp: () -> Unit,
    onDismissUpdateDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_v2),
            contentDescription = stringResource(id = R.string.splash_app_icon),
            modifier = Modifier.align(Alignment.Center),
        )

        AnimatedVisibility(
            visible = screenState == SplashScreenState.LOADING,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KuringTheme.colors.gray300.copy(alpha = 0.5f)),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = KuringTheme.colors.mainPrimary,
                )
            }
        }

        AnimatedVisibility(
            visible = screenState == SplashScreenState.UPDATE_REQUIRED,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            UpdateAppDialog(
                onUpdate = onUpdateApp, onDismiss = onDismissUpdateDialog,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@LightAndDarkPreview
@Composable
private fun SplashScreenPreview() {
    KuringTheme {
        SplashScreen(
            screenState = SplashScreenState.UPDATE_NOT_REQUIRED,
            onUpdateApp = {},
            onDismissUpdateDialog = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}