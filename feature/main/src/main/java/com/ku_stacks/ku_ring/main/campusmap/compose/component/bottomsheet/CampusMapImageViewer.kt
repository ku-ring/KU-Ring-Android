package com.ku_stacks.ku_ring.main.campusmap.compose.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.main.R

private val ImageViewerBackground = Color(0xFF434343)
private val ImageViewerMaxHeight = 600.dp
private const val ImageViewerMaxScale = 4f

@Composable
internal fun CampusMapImageViewer(
    imageUrl: String,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
        ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(ImageViewerBackground),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .padding(end = 4.dp),
                ) {
                    ImageViewerCloseButton(onClick = onDismissRequest)
                }

                ZoomableCampusMapImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ImageViewerMaxHeight)
                        .clipToBounds(),
                )
            }
        }
    }
}

@Composable
private fun ImageViewerCloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(KuringTheme.colors.gray300),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_x_v2),
                contentDescription = stringResource(id = R.string.campus_map_close_image_viewer),
                tint = KuringTheme.colors.background,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

@Composable
private fun ZoomableCampusMapImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
) {
    var scale by remember(imageUrl) { mutableFloatStateOf(1f) }
    var offset by remember(imageUrl) { mutableStateOf(Offset.Zero) }
    var containerSize by remember(imageUrl) { mutableStateOf(IntSize.Zero) }
    val latestScale = rememberUpdatedState(scale)
    val latestOffset = rememberUpdatedState(offset)
    val latestContainerSize = rememberUpdatedState(containerSize)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged { containerSize = it }
            .pointerInput(imageUrl) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val currentContainerSize = latestContainerSize.value
                    val currentScale = latestScale.value
                    val newScale = (currentScale * zoom).coerceIn(1f, ImageViewerMaxScale)
                    val scaleChange = newScale / currentScale
                    val center = Offset(
                        x = currentContainerSize.width / 2f,
                        y = currentContainerSize.height / 2f,
                    )
                    val requestedOffset = latestOffset.value + pan +
                        (centroid - center) * (1f - scaleChange)

                    scale = newScale
                    offset = if (newScale == 1f) {
                        Offset.Zero
                    } else {
                        val maxOffsetX = currentContainerSize.width * (newScale - 1f) / 2f
                        val maxOffsetY = currentContainerSize.height * (newScale - 1f) / 2f
                        Offset(
                            x = requestedOffset.x.coerceIn(-maxOffsetX, maxOffsetX),
                            y = requestedOffset.y.coerceIn(-maxOffsetY, maxOffsetY),
                        )
                    }
                }
            },
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(id = R.string.campus_map_view_place_image),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                },
        )
    }
}
