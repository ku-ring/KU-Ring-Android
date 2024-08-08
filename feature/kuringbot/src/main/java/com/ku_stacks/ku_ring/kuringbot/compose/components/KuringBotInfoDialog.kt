package com.ku_stacks.ku_ring.kuringbot.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.kuringbot.R

@Composable
internal fun KuringBotInfoDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dialogAnimationSpec = tween<Float>(durationMillis = 500)

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(dialogAnimationSpec),
        exit = fadeOut(dialogAnimationSpec),
        modifier = modifier,
    ) {
        KuringBotInfoDialogContainer(onDismiss = onDismiss)
    }
}

@Composable
private fun KuringBotInfoDialogContainer(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .zIndex(1f)
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = interactionSource,
            ),
    ) {
        KuringBotInfoContents(
            modifier = Modifier
                .clickable(
                    onClick = {},
                    indication = null,
                    interactionSource = interactionSource
                )
                .fillMaxWidth(253 / 375f)
                .padding(end = 16.dp)
                .align(Alignment.TopEnd),
        )
    }
}

@Composable
private fun KuringBotInfoContents(
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clip(shape)
            .border(1.dp, KuringTheme.colors.gray200, shape)
            .background(KuringTheme.colors.background)
            .padding(top = 10.dp, end = 16.dp, bottom = 10.dp, start = 10.dp),
    ) {
        val infoTokens = stringResource(id = R.string.kuringbot_info).split("\n")
        infoTokens.forEach { token ->
            KuringBotInfoContent(token)
        }
    }
}

@Composable
private fun KuringBotInfoContent(
    token: String,
    modifier: Modifier = Modifier,
) {
    val style = TextStyle(
        fontSize = 15.sp,
        lineHeight = 24.45.sp,
        fontFamily = Pretendard,
        fontWeight = FontWeight(500),
        color = KuringTheme.colors.textBody,
    )
    CompositionLocalProvider(LocalTextStyle provides style) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Top,
        ) {
            Text(text = "  ·  ")
            Text(token)
        }
    }
}

@LightAndDarkPreview
@Composable
private fun KuringBotInfoDialogPreview() {
    KuringTheme {
        KuringBotInfoDialog(
            isVisible = true,
            onDismiss = {},
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        )
    }
}