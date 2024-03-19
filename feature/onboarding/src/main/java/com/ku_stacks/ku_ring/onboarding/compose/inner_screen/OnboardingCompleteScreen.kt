package com.ku_stacks.ku_ring.onboarding.compose.inner_screen

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.ku_stacks.ku_ring.designsystem.components.KuringCallToAction
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.onboarding.R

@Composable
internal fun OnboardingCompleteScreen(
    onStartKuring: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.background(KuringTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(132.dp))
        OnboardingCompleteTitle()
        OnboardingCompleteCaption(modifier = Modifier.padding(top = 12.dp))
        Spacer(modifier = Modifier.weight(4f))
        OnboardingCompleteGif(modifier = Modifier.size(150.dp))
        Spacer(modifier = Modifier.weight(5f))
        KuringCallToAction(
            onClick = onStartKuring,
            text = stringResource(id = R.string.onboarding_complete_screen_cta_text),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun OnboardingCompleteTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.onboarding_complete_screen_title),
        style = TextStyle(
            fontSize = 24.sp,
            lineHeight = 34.08.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(700),
            color = KuringTheme.colors.textTitle,
        ),
        modifier = modifier,
    )
}

@Composable
private fun OnboardingCompleteCaption(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.onboarding_complete_screen_caption),
        style = TextStyle(
            fontSize = 15.sp,
            lineHeight = 24.45.sp,
            fontFamily = Pretendard,
            fontWeight = FontWeight(500),
            color = KuringTheme.colors.textCaption1,
            textAlign = TextAlign.Center,
        ),
        modifier = modifier,
    )
}

@Composable
private fun OnboardingCompleteGif(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader(context).newBuilder()
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }.build()
    }

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(R.drawable.onboarding_complete)
            .decoderFactory(imageLoader.components.decoderFactories.first())
            .build(),
        contentDescription = null,
        modifier = modifier,
    )
}

@LightAndDarkPreview
@Composable
private fun OnboardingCompletePreview() {
    KuringTheme {
        OnboardingCompleteScreen(
            onStartKuring = { },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}