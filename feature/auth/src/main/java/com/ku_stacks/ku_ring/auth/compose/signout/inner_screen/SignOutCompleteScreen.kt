package com.ku_stacks.ku_ring.auth.compose.signout.inner_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.auth.compose.component.button.RoundedCornerButton
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R.drawable.ic_app_v2
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_complete_app_icon
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_complete_button
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_complete_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_complete_top_bar_sub_heading

@Composable
internal fun SignOutCompleteScreen(
    onNavigateToMyPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onNavigateToMyPage()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background),
    ) {
        AuthTopBar(
            headingText = stringResource(sign_out_complete_top_bar_heading),
            subHeadingText = stringResource(sign_out_complete_top_bar_sub_heading),
            onBackButtonClick = onNavigateToMyPage,
        )

        Image(
            painter = painterResource(id = ic_app_v2),
            contentDescription = stringResource(id = sign_out_complete_app_icon),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(end = 5.dp, bottom = 33.dp)
                .width(230.dp)
                .weight(1f),
        )

        RoundedCornerButton(
            text = stringResource(sign_out_complete_button),
            colors = ButtonDefaults.textButtonColors(
                containerColor = KuringTheme.colors.warning,
                contentColor = KuringTheme.colors.background,
            ),
            onClick = onNavigateToMyPage,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
        )
    }
}


@LightAndDarkPreview
@Composable
private fun SignOutPreview() {
    KuringTheme {
        SignOutCompleteScreen(
            onNavigateToMyPage = {}
        )
    }
}