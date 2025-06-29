package com.ku_stacks.ku_ring.auth.compose.signup.inner_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.auth.R
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_complete_button_proceed
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_complete_heading
import com.ku_stacks.ku_ring.feature.auth.R.string.sign_up_complete_sub_heading

@Composable
internal fun SignUpCompleteScreen(
    onNavigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = onNavigateToSignIn)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(KuringTheme.colors.background),
    ) {
        Text(
            text = stringResource(sign_up_complete_heading),
            modifier = Modifier.padding(top = 124.dp),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp,
                color = KuringTheme.colors.textTitle,
            )
        )

        Text(
            text = stringResource(sign_up_complete_sub_heading),
            modifier = Modifier.padding(top = 12.dp),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 24.sp,
                color = KuringTheme.colors.textCaption1,
            )
        )

        Spacer(modifier = Modifier.weight(0.2f))

        SignUpCompleteAnimation(
            modifier = Modifier
                .padding(horizontal = 80.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.weight(0.4f))


        KuringCallToAction(
            onClick = onNavigateToSignIn,
            text = stringResource(sign_up_complete_button_proceed),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
        )
    }
}

@Composable
private fun SignUpCompleteAnimation(
    modifier: Modifier = Modifier,
) {
    val checkLottieComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sign_up_complete_animation))
    LottieAnimation(
        composition = checkLottieComposition,
        iterations = Int.MAX_VALUE,
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun SignUpCompleteScreenPreview() {
    KuringTheme {
        SignUpCompleteScreen(
            onNavigateToSignIn = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
