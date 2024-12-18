package com.ku_stacks.ku_ring.library.compose.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.feature.library.R

@Composable
internal fun SeatStatusReloadFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val rotation = remember { Animatable(DEFAULT_ANIMATION_VALUE) }

    val iconTint by animateColorAsState(
        targetValue = if (isLoading) KuringTheme.colors.mainPrimary else KuringTheme.colors.gray300,
        label = "LibraryFAB"
    )

    LaunchedEffect(isLoading) {
        if (isLoading) {
            rotation.animateTo(
                targetValue = rotation.value + TARGET_ANIMATION_VALUE,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = ANIMATION_DURATION_IN_MILLIS,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    FloatingActionButton(
        onClick = onClick,
        backgroundColor = KuringTheme.colors.background,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp),
        modifier = modifier,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_refresh_v2),
                contentDescription = null,
                modifier = Modifier
                    .padding(11.dp)
                    .graphicsLayer(
                        rotationZ = rotation.value,
                        transformOrigin = TransformOrigin.Center
                    ),
                tint = iconTint
            )
        }
    }
}

private const val DEFAULT_ANIMATION_VALUE = 0f
private const val TARGET_ANIMATION_VALUE = 360f
private const val ANIMATION_DURATION_IN_MILLIS = 1000

@LightAndDarkPreview
@Composable
private fun SeatStatusReloadFabPreview() {
    KuringTheme {
        var loadState by remember { mutableStateOf(false) }

        SeatStatusReloadFab(
            onClick = { loadState = !loadState },
            isLoading = loadState
        )
    }
}