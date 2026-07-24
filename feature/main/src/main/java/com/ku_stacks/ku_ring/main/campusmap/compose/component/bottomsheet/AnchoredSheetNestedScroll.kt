package com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity

internal fun <T> anchoredSheetNestedScrollConnection(
    state: AnchoredDraggableState<T>,
    flingBehavior: TargetedFlingBehavior,
): NestedScrollConnection = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        return if (available.y < 0f && source == NestedScrollSource.UserInput) {
            Offset(x = 0f, y = state.dispatchRawDelta(available.y))
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        return if (source == NestedScrollSource.UserInput) {
            Offset(x = 0f, y = state.dispatchRawDelta(available.y))
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        if (
            available.y < 0f &&
            !state.offset.isNaN() &&
            state.offset > state.anchors.minPosition()
        ) {
            state.performAnchoredSheetFling(flingBehavior, available.y)
            return available
        }
        return Velocity.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        if (!state.offset.isNaN() && state.anchors.size > 0) {
            state.performAnchoredSheetFling(flingBehavior, available.y)
        }
        return available
    }
}

private suspend fun <T> AnchoredDraggableState<T>.performAnchoredSheetFling(
    flingBehavior: TargetedFlingBehavior,
    velocity: Float,
) {
    anchoredDrag { latestAnchors ->
        val flingScope = object : ScrollScope {
            override fun scrollBy(pixels: Float): Float {
                val previousOffset = offset
                val newOffset = (previousOffset + pixels).coerceIn(
                    minimumValue = latestAnchors.minPosition(),
                    maximumValue = latestAnchors.maxPosition(),
                )
                dragTo(newOffset)
                return newOffset - previousOffset
            }
        }
        with(flingBehavior) {
            flingScope.performFling(velocity)
        }
    }
}
