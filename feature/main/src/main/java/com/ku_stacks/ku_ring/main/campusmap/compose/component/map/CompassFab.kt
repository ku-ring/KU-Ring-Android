package com.ku_stacks.ku_ring.main.campusmap.compose.component.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard
import com.ku_stacks.ku_ring.main.R
import kotlin.math.abs

internal const val NORTH_BEARING_TOLERANCE_DEGREES = 2.0

private const val COMPASS_RING_ROTATION_DEGREES = -105f
private const val COMPASS_NEEDLE_ROTATION_DEGREES = 45f
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
    val secondaryColor = KuringTheme.colors.gray200

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(compassRotationDegrees(bearing)),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_campus_map_compass_ring),
                contentDescription = null,
                colorFilter = ColorFilter.tint(secondaryColor),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(width = 31.4.dp, height = 31.6.dp)
                    .rotate(COMPASS_RING_ROTATION_DEGREES),
            )

            Text(
                text = "N",
                color = KuringTheme.colors.textTitle,
                fontSize = 8.sp,
                fontFamily = Pretendard,
                fontWeight = FontWeight.Bold,
                lineHeight = 8.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_campus_map_compass_tick_east),
                contentDescription = null,
                colorFilter = ColorFilter.tint(secondaryColor),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 6.dp)
                    .size(width = 5.6.dp, height = 1.6.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_campus_map_compass_tick_west),
                contentDescription = null,
                colorFilter = ColorFilter.tint(secondaryColor),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 7.dp)
                    .size(width = 4.6.dp, height = 1.6.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_campus_map_compass_tick_south),
                contentDescription = null,
                colorFilter = ColorFilter.tint(secondaryColor),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp)
                    .size(width = 1.6.dp, height = 5.6.dp),
            )

            Image(
                painter = painterResource(R.drawable.ic_campus_map_compass_needle),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 2.dp)
                    .size(width = 12.6.dp, height = 13.2.dp)
                    .rotate(COMPASS_NEEDLE_ROTATION_DEGREES),
            )
        }
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
