package com.ku_stacks.ku_ring.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorizedDecayAnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalAbsoluteElevation
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.Switch
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

/**
 * Kuring style Switch, which is similar to Material [Switch].
 *
 * Switches toggle the state of a single item on or off.
 *
 *
 * @param checked whether or not this component is checked
 * @param onCheckedChange callback to be invoked when Switch is being clicked,
 * therefore the change of checked state is requested.  If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 * @param modifier Modifier to be applied to the switch layout
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Switch. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Switch in different [Interaction]s.
 * @param colors [KuringSwitchColors] that will be used to determine the color of the thumb and track
 * in different states. See [KuringSwitchDefaults.colors].
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KuringSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: KuringSwitchColors = KuringSwitchDefaults.colors(),
) {
    val minBound = with(LocalDensity.current) { (ThumbDiameter / 20).toPx() }
    val maxBound = with(LocalDensity.current) { (ThumbPathLength - ThumbDiameter / 20).toPx() }
    // If we reach a bound and settle, we invoke onCheckedChange with the new value. If the user
    // does not update `checked`, we would now be in an invalid state. We keep track of the
    // the animation state through this, animating back to the previous value if we don't receive
    // a new checked value.
    var forceAnimationCheck by remember { mutableStateOf(false) }
    val switchVelocityThresholdPx = with(LocalDensity.current) { SwitchVelocityThreshold.toPx() }
    val anchoredDraggableState = remember(maxBound, switchVelocityThresholdPx) {
        AnchoredDraggableState(
            initialValue = checked,
            anchors = DraggableAnchors {
                false at minBound
                true at maxBound
            },
            positionalThreshold = { distance: Float -> distance * SwitchPositionalThreshold },
            velocityThreshold = { switchVelocityThresholdPx },
            snapAnimationSpec = AnimationSpec,
            decayAnimationSpec = object : DecayAnimationSpec<Float> {
                override fun <V : AnimationVector> vectorize(typeConverter: TwoWayConverter<Float, V>): VectorizedDecayAnimationSpec<V> {
                    return vectorize(typeConverter)
                }
            },
        )
    }
    val currentOnCheckedChange by rememberUpdatedState(onCheckedChange)
    val currentChecked by rememberUpdatedState(checked)
    LaunchedEffect(anchoredDraggableState) {
        snapshotFlow { anchoredDraggableState.currentValue }
            .collectLatest { newValue ->
                if (currentChecked != newValue) {
                    currentOnCheckedChange?.invoke(newValue)
                    forceAnimationCheck = !forceAnimationCheck
                }
            }
    }
    LaunchedEffect(checked, forceAnimationCheck) {
        if (checked != anchoredDraggableState.currentValue) {
            anchoredDraggableState.animateTo(checked)
        }
    }
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val toggleableModifier =
        if (onCheckedChange != null) {
            Modifier.toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null
            )
        } else {
            Modifier
        }

    Box(
        modifier
            .then(
                if (onCheckedChange != null) {
                    Modifier.minimumInteractiveComponentSize()
                } else {
                    Modifier
                }
            )
            .then(toggleableModifier)
            .anchoredDraggable(
                state = anchoredDraggableState,
                orientation = Orientation.Horizontal,
                enabled = enabled && onCheckedChange != null,
                reverseDirection = isRtl,
                interactionSource = interactionSource,
                startDragImmediately = false
            )
            .wrapContentSize(Alignment.Center)
            .padding(DefaultSwitchPadding)
            .requiredSize(SwitchWidth, SwitchHeight)
    ) {
        SwitchImpl(
            checked = anchoredDraggableState.targetValue,
            enabled = enabled,
            colors = colors,
            thumbValue = { anchoredDraggableState.requireOffset() },
            interactionSource = interactionSource
        )
    }
}

@Stable
interface KuringSwitchColors {

    /**
     * Represents the color used for the switch's thumb, depending on [enabled] and [checked].
     *
     * @param enabled whether the [Switch] is enabled or not
     * @param checked whether the [Switch] is checked or not
     */
    @Composable
    fun thumbColor(enabled: Boolean, checked: Boolean): State<Color>

    /**
     * Represents the color used for the switch's track, depending on [enabled] and [checked].
     *
     * @param enabled whether the [Switch] is enabled or not
     * @param checked whether the [Switch] is checked or not
     */
    @Composable
    fun trackColor(enabled: Boolean, checked: Boolean): State<Color>
}


@Composable
private fun BoxScope.SwitchImpl(
    checked: Boolean,
    enabled: Boolean,
    colors: KuringSwitchColors,
    thumbValue: () -> Float,
    interactionSource: InteractionSource
) {
    val interactions = remember { mutableStateListOf<Interaction>() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> interactions.add(interaction)
                is PressInteraction.Release -> interactions.remove(interaction.press)
                is PressInteraction.Cancel -> interactions.remove(interaction.press)
                is DragInteraction.Start -> interactions.add(interaction)
                is DragInteraction.Stop -> interactions.remove(interaction.start)
                is DragInteraction.Cancel -> interactions.remove(interaction.start)
            }
        }
    }

    val hasInteraction = interactions.isNotEmpty()
    val elevation = if (hasInteraction) {
        ThumbPressedElevation
    } else {
        ThumbDefaultElevation
    }
    val trackColor by colors.trackColor(enabled, checked)
    Canvas(
        Modifier
            .align(Alignment.Center)
            .fillMaxSize()
    ) {
        drawTrack(trackColor, TrackWidth.toPx(), TrackStrokeWidth.toPx())
    }
    val thumbColor by colors.thumbColor(enabled, checked)
    val elevationOverlay = LocalElevationOverlay.current
    val absoluteElevation = LocalAbsoluteElevation.current + elevation
    val resolvedThumbColor by animateColorAsState(
        if (thumbColor == KuringTheme.colors.background && elevationOverlay != null) {
            elevationOverlay.apply(thumbColor, absoluteElevation)
        } else {
            thumbColor
        }
    )
    Spacer(
        Modifier
            .align(Alignment.CenterStart)
            .offset { IntOffset(thumbValue().roundToInt(), 0) }
            .indication(
                interactionSource = interactionSource,
                indication = ripple(bounded = false, radius = ThumbRippleRadius)
            )
            .requiredSize(ThumbDiameter)
            .shadow(elevation, CircleShape, clip = false)
            .background(resolvedThumbColor, CircleShape)
    )
}

private fun DrawScope.drawTrack(trackColor: Color, trackWidth: Float, strokeWidth: Float) {
    val strokeRadius = strokeWidth / 2
    drawLine(
        trackColor,
        Offset(strokeRadius, center.y),
        Offset(trackWidth - strokeRadius, center.y),
        strokeWidth,
        StrokeCap.Round
    )
}

internal val TrackWidth = 34.dp
internal val TrackStrokeWidth = 20.dp
internal val ThumbDiameter = TrackStrokeWidth * 0.9f

private val ThumbRippleRadius = 24.dp

private val DefaultSwitchPadding = 2.dp
private val SwitchWidth = TrackWidth
private val SwitchHeight = ThumbDiameter
private val ThumbPathLength = TrackWidth - ThumbDiameter

private val AnimationSpec = TweenSpec<Float>(durationMillis = 100)

private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp

object KuringSwitchDefaults {
    /**
     * Creates a [KuringSwitchColors] that represents the different colors used in a [KuringSwitch] in
     * different states.
     *
     * @param thumbColor the color used for the thumb
     * @param checkedTrackColor the color used for the track when enabled and checked
     * @param uncheckedTrackColor the color used for the track when enabled and unchecked
     * @param uncheckedTrackAlpha the alpha applied to [uncheckedTrackColor] and
     * [disabledUncheckedTrackColor]
     * @param disabledThumbColor the color used for the thumb when disabled
     * @param disabledCheckedTrackColor the color used for the track when disabled and checked
     * @param disabledUncheckedTrackColor the color used for the track when disabled and unchecked
     */
    @Composable
    fun colors(
        thumbColor: Color = KuringTheme.colors.background,
        checkedTrackColor: Color = KuringTheme.colors.mainPrimary,
        uncheckedTrackColor: Color = KuringTheme.colors.textTitle,
        uncheckedTrackAlpha: Float = 0.38f,
        disabledThumbColor: Color = thumbColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(KuringTheme.colors.background),
        disabledCheckedTrackColor: Color = checkedTrackColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(KuringTheme.colors.background),
        disabledUncheckedTrackColor: Color = uncheckedTrackColor
            .copy(alpha = ContentAlpha.disabled)
            .compositeOver(KuringTheme.colors.background)
    ): KuringSwitchColors = DefaultKuringSwitchColors(
        thumbColor = thumbColor,
        checkedTrackColor = checkedTrackColor,
        uncheckedTrackColor = uncheckedTrackColor.copy(alpha = uncheckedTrackAlpha),
        disabledThumbColor = disabledThumbColor,
        disabledCheckedTrackColor = disabledCheckedTrackColor,
        disabledUncheckedTrackColor = disabledUncheckedTrackColor.copy(alpha = uncheckedTrackAlpha)
    )
}

/**
 * Default [KuringSwitchColors] implementation.
 */
@Immutable
private class DefaultKuringSwitchColors(
    private val thumbColor: Color,
    private val checkedTrackColor: Color,
    private val uncheckedTrackColor: Color,
    private val disabledThumbColor: Color,
    private val disabledCheckedTrackColor: Color,
    private val disabledUncheckedTrackColor: Color
) : KuringSwitchColors {
    @Composable
    override fun thumbColor(enabled: Boolean, checked: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) thumbColor else disabledThumbColor)
    }

    @Composable
    override fun trackColor(enabled: Boolean, checked: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                if (checked) checkedTrackColor else uncheckedTrackColor
            } else {
                if (checked) disabledCheckedTrackColor else disabledUncheckedTrackColor
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultKuringSwitchColors

        if (thumbColor != other.thumbColor) return false
        if (checkedTrackColor != other.checkedTrackColor) return false
        if (uncheckedTrackColor != other.uncheckedTrackColor) return false
        if (disabledThumbColor != other.disabledThumbColor) return false
        if (disabledCheckedTrackColor != other.disabledCheckedTrackColor) return false
        if (disabledUncheckedTrackColor != other.disabledUncheckedTrackColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = thumbColor.hashCode()
        result = 31 * result + checkedTrackColor.hashCode()
        result = 31 * result + uncheckedTrackColor.hashCode()
        result = 31 * result + disabledThumbColor.hashCode()
        result = 31 * result + disabledCheckedTrackColor.hashCode()
        result = 31 * result + disabledUncheckedTrackColor.hashCode()
        return result
    }
}

private const val SwitchPositionalThreshold = 0.7f
private val SwitchVelocityThreshold = 125.dp


@LightAndDarkPreview
@Composable
private fun KuringSwitchPreview() {
    var checked by remember { mutableStateOf(true) }
    KuringTheme {
        KuringSwitch(
            checked = checked,
            onCheckedChange = { checked = !checked },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .padding(16.dp),
        )
    }
}