package com.ku_stacks.ku_ring.auth.compose.signup.inner_screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.auth.compose.component.topbar.AuthTopBar
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_check_circle_fill_2_v2
import com.ku_stacks.ku_ring.designsystem.R.drawable.ic_check_circle_fill_v2
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_conditions_body
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_conditions_button_proceed
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_conditions_request
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_conditions_request_approved
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_conditions_request_declined
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_top_bar_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_terms_top_bar_sub_heading


@Composable
internal fun TermsAndConditionsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToEmailVerification: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isApproved by remember { mutableStateOf(false) }

    TermsScreen(
        isApproved = isApproved,
        onRadioButtonClick = { isApproved = it },
        onProceedButtonClick = onNavigateToEmailVerification,
        onBackButtonClick = onNavigateUp,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TermsScreen(
    isApproved: Boolean,
    onRadioButtonClick: (Boolean) -> Unit,
    onProceedButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background),
    ) {
        AuthTopBar(
            headingText = stringResource(sign_up_terms_top_bar_heading),
            subHeadingText = stringResource(sign_up_terms_top_bar_sub_heading),
            onBackButtonClick = onBackButtonClick,
        )

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 45.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.15f)
                    .border(width = 1.dp, color = KuringTheme.colors.gray200)
                    .verticalScroll(scrollState),
            ) {
                Spacer(modifier = Modifier.padding(top = 18.dp))
                Text(
                    text = stringResource(sign_up_terms_conditions_body),
                    style = TextStyle(
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        fontFamily = Pretendard,
                        fontWeight = FontWeight(500),
                        color = KuringTheme.colors.textCaption1,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }
        }

        UserGrantRadioButtonGroup(
            isApproved = isApproved,
            onClick = onRadioButtonClick,
            modifier = Modifier.padding(top = 13.dp, start = 20.dp, end = 20.dp),
        )

        Spacer(modifier = Modifier.weight(1f))

        KuringCallToAction(
            text = stringResource(sign_up_terms_conditions_button_proceed),
            onClick = onProceedButtonClick,
            enabled = isApproved,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
internal fun UserGrantRadioButtonGroup(
    isApproved: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(
            text = stringResource(sign_up_terms_conditions_request),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textBody,
            ),
        )

        UserGrantRadioButton(
            selected = isApproved,
            text = stringResource(sign_up_terms_conditions_request_approved),
            onClick = { onClick(true) },
        )

        UserGrantRadioButton(
            selected = !isApproved,
            text = stringResource(sign_up_terms_conditions_request_declined),
            onClick = { onClick(false) },
        )
    }
}

@Composable
private fun UserGrantRadioButton(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Crossfade(
            targetState = selected,
            modifier = Modifier.clickable { onClick() },
            label = "department check/uncheck",
        ) {
            if (it) CheckIcon() else UncheckIcon()
        }

        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 24.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight(500),
                color = KuringTheme.colors.textBody,
            )
        )
    }
}

@Composable
private fun CheckIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(ic_check_circle_fill_v2),
        contentDescription = stringResource(sign_up_terms_conditions_request_approved),
        tint = KuringTheme.colors.mainPrimary,
        modifier = modifier
            .size(24.dp)
            .padding(2.dp),
    )
}

@Composable
private fun UncheckIcon(modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(ic_check_circle_fill_2_v2),
        contentDescription = stringResource(sign_up_terms_conditions_request_declined),
        tint = KuringTheme.colors.gray300,
        modifier = modifier
            .size(24.dp)
            .padding(2.dp),
    )
}

@LightAndDarkPreview
@Composable
private fun TermsScreenPreview() {
    KuringTheme {
        var selected by remember { mutableStateOf(false) }
        TermsScreen(
            isApproved = selected,
            onRadioButtonClick = { selected = it },
            onProceedButtonClick = {},
            onBackButtonClick = {},
        )
    }
}