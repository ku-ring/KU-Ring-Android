package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R
import kotlin.math.abs

internal const val NORTH_BEARING_TOLERANCE_DEGREES = 2.0

private const val COMPASS_ANIMATION_DURATION_MILLIS = 150
private val CompassSize = 44.dp

@Composable
internal fun CompassFab(
    bearing: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = shouldShowCompass(bearing),
        enter = fadeIn(tween(COMPASS_ANIMATION_DURATION_MILLIS)) +
            scaleIn(
                animationSpec = tween(COMPASS_ANIMATION_DURATION_MILLIS),
                initialScale = 0.8f,
            ),
        exit = fadeOut(tween(COMPASS_ANIMATION_DURATION_MILLIS)) +
            scaleOut(
                animationSpec = tween(COMPASS_ANIMATION_DURATION_MILLIS),
                targetScale = 0.8f,
            ),
        modifier = modifier,
    ) {
        CompassButton(
            bearing = bearing,
            onClick = onClick,
        )
    }
}

@Composable
private fun CompassButton(
    bearing: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resetBearingDescription = stringResource(R.string.campus_map_reset_bearing)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(CompassSize)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(KuringTheme.colors.background)
            .semantics {
                contentDescription = resetBearingDescription
            }
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_campus_map_compass),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .fillMaxSize()
                .rotate(compassRotationDegrees(bearing)),
        )
    }
}

internal fun shouldShowCompass(bearing: Double): Boolean {
    return angularDistanceFromNorth(bearing) > NORTH_BEARING_TOLERANCE_DEGREES
}

internal fun angularDistanceFromNorth(bearing: Double): Double {
    return abs(normalizedSignedBearing(bearing))
}

internal fun compassRotationDegrees(bearing: Double): Float {
    val normalizedBearing = normalizedSignedBearing(bearing)
    return if (normalizedBearing == 0.0) 0f else -normalizedBearing.toFloat()
}

private fun normalizedSignedBearing(bearing: Double): Double {
    if (!bearing.isFinite()) return 0.0
    return ((bearing + 180.0) % 360.0 + 360.0) % 360.0 - 180.0
}

@LightAndDarkPreview
@Composable
private fun CompassFabPreview() {
    KuringTheme {
        CompassFab(
            bearing = 45.0,
            onClick = {},
        )
    }
}
