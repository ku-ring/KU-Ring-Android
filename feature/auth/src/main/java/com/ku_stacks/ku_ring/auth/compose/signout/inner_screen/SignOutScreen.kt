package com.ku_stacks.ku_ring.auth.compose.signout.inner_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ku_stacks.ku_ring.auth.compose.component.button.RoundedCornerButton
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.auth.compose.signout.SignOutViewModel
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_button_confirm
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_info_content_1
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_info_content_2
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_info_title_1
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_info_title_2
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_out_top_bar_sub_heading

@Composable
internal fun SignOutScreen(
    onNavigateUp: () -> Unit,
    onNavigateToSignOutComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignOutViewModel = hiltViewModel()
) {
    var isConfirmDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.isSignOutSuccess) {
        if (viewModel.isSignOutSuccess) onNavigateToSignOutComplete()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background)
    ) {
        AuthTopBar(
            headingText = stringResource(sign_out_top_bar_heading),
            subHeadingText = stringResource(sign_out_top_bar_sub_heading),
            onBackButtonClick = onNavigateUp
        )

        TextBox(
            title = stringResource(sign_out_info_title_1),
            content = stringResource(sign_out_info_content_1),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 45.dp)
                .fillMaxWidth()
        )

        TextBox(
            title = stringResource(sign_out_info_title_2),
            content = stringResource(sign_out_info_content_2),
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        RoundedCornerButton(
            text = stringResource(sign_out_button_confirm),
            colors = ButtonDefaults.textButtonColors(
                containerColor = KuringTheme.colors.warning,
                contentColor = KuringTheme.colors.background,
            ),
            onClick = { isConfirmDialogVisible = true },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        )
    }

    AnimatedVisibility(
        visible = isConfirmDialogVisible
    ) {
        SignOutDialog(
            onDismiss = { isConfirmDialogVisible = false },
            onConfirm = viewModel::signOut
        )
    }
}

@Composable
private fun TextBox(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier
            .background(
                color = KuringTheme.colors.gray100,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(
                horizontal = 16.dp,
                vertical = 13.dp,
            )
    ) {
        Text(
            text = title,
            style = TextStyle.Default.copy(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                color = KuringTheme.colors.textBody,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None,
                ),
            ),
        )

        Text(
            text = content,
            style = TextStyle.Default.copy(
                fontSize = 14.sp,
                lineHeight = (14 * 1.63f).sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                color = KuringTheme.colors.textBody,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.None,
                ),
            ),
        )
    }
}


@LightAndDarkPreview
@Composable
private fun SignOutPreview() {
    KuringTheme {
        SignOutScreen(
            onNavigateUp = {},
            onNavigateToSignOutComplete = {}
        )
    }
}