package com.ku_stacks.ku_ring.designsystem.components.dragdrop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ku_stacks.ku_ring.designsystem.components.LightAndDarkPreview
import com.ku_stacks.ku_ring.designsystem.kuringtheme.KuringTheme
import com.ku_stacks.ku_ring.designsystem.kuringtheme.values.Pretendard

/**
 * Draggable lazy column, inspired by [Android Code Search](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/foundation/foundation/integration-tests/foundation-demos/src/main/java/androidx/compose/foundation/demos/LazyColumnDragAndDropDemo.kt).
 * Each item becomes draggable when long-clicked.
 * When all pointers are up, the item is released and becomes non-draggable.
 *
 * @param items List of items to display.
 * @param onMove Callback invoked when an item is moved.
 * @param key An unique key of the item.
 * @param modifier Modifier to be applied to the layout.
 * @param contentPadding Padding around the content.
 * @param reverseLayout Whether the items should be reversed. When `true`, items are laid out
 * in the reverse order and `LazyListState.firstVisibleItemIndex == 0` means that colum is scrolled
 * to the bottom.
 * @param verticalArrangement The vertical arrangement of the layout's children.
 * @param horizontalAlignment The horizontal alignment applied to the items.
 * @param flingBehavior logic describing fling behavior.
 * @param userScrollEnabled whether the scrolling via the user gestures or accessibility actions
 * is allowed. The list can be scrolled programmatically even when this value is `false`.
 * @param contentType a factory of the content types for the item. The item compositions of the same
 * type could be reused more efficiently. Note that null is a valid type and items of such type
 * will be considered compatible.
 * @param content a block which describes the content.
 */
@Composable
fun DraggableLazyColumn(
    items: List<DraggableLazyColumnItem>,
    onMove: (Int, Int) -> Unit,
    key: ((index: Int, item: DraggableLazyColumnItem) -> Any),
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    contentType: (index: Int, item: DraggableLazyColumnItem) -> Any? = { _, _ -> null },
    content: @Composable LazyItemScope.(index: Int, item: DraggableLazyColumnItem, isDraggable: Boolean) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val dragDropState = rememberDragDropState(lazyListState) { from, to ->
        onMove(from, to)
    }

    LazyColumn(
        modifier = modifier.dragContainer(dragDropState),
        state = lazyListState,
        contentPadding = contentPadding,
        flingBehavior = flingBehavior,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
        reverseLayout = reverseLayout,
        userScrollEnabled = userScrollEnabled,
    ) {
        itemsIndexed(
            items = items,
            key = key,
            contentType = contentType,
        ) { index, item ->
            DraggableItem(
                dragDropState = dragDropState,
                index = index,
            ) { isDraggable ->
                content(index, item, isDraggable)
            }
        }
    }
}

fun Modifier.dragContainer(dragDropState: DragDropState): Modifier {
    return pointerInput(dragDropState) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset = offset)
            },
            onDragStart = { offset -> dragDropState.onDragStart(offset) },
            onDragEnd = { dragDropState.onDragInterrupted() },
            onDragCancel = { dragDropState.onDragInterrupted() }
        )
    }
}

@LightAndDarkPreview
@Composable
private fun DraggableLazyColumnPreview() {
    data class Item(val id: Int) : DraggableLazyColumnItem

    var items by remember {
        mutableStateOf(List(20) { Item(it) })
    }

    KuringTheme {
        DraggableLazyColumn(
            items = items,
            onMove = { from, to ->
                items = items.toMutableList().apply { move(from, to) } // Important!
            },
            key = { index, item ->
                (item as Item).id
            },
            modifier = Modifier
                .background(KuringTheme.colors.background)
                .fillMaxSize(),
        ) { index, draggableLazyColumnItem, isDraggable ->
            Text(
                text = (draggableLazyColumnItem as Item).id.toString(),
                modifier = Modifier
                    .border(width = 1.dp, color = Color.Red)
                    .padding(start = 10.dp, top = 20.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 24.45.sp,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight(500),
                    color = KuringTheme.colors.textBody,
                )
            )
        }
    }
}