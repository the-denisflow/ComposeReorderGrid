package io.github.thedenisflow.reordergrid.draganddrop.tile.component

import android.content.ClipData
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import io.github.thedenisflow.reordergrid.draganddrop.tile.state.DragAndDropState

@Composable
fun DraggableItemContainer(
    key: Any,
    dragAndDropState: DragAndDropState,
    clipDataText: String,
    content: @Composable () -> Unit

) {
    val itemGraphicsLayer = rememberGraphicsLayer()
    var itemBouns by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(Unit) {
        dragAndDropState.localView.setOnDragListener(dragAndDropState)
    }

    Box(
        modifier = Modifier
            .dragShadowSource(
                key = key,
                itemGraphicsLayer = itemGraphicsLayer,
                dragAndDropState = dragAndDropState,
                onBoundsChanged = { itemBouns = it }
            )
            .wrapContentSize()
            .detectDragGesture(
                key = key,
                dragAndDropState = dragAndDropState,
                itemBounds = itemBouns,
                itemGraphicsLayer = itemGraphicsLayer,
                label = clipDataText
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun Modifier.detectDragGesture(
    key: Any,
    dragAndDropState: DragAndDropState,
    itemBounds: Rect,
    itemGraphicsLayer: GraphicsLayer,
    label: String
): Modifier = this.pointerInput(Unit) {
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            startTileDrag(
                key = key,
                offset = offset,
                label = label,
                dragAndDropState = dragAndDropState,
                itemBounds = itemBounds,
                itemGraphicsLayer = itemGraphicsLayer
            )
        },
        onDrag = { change, _ ->
            change.consume()
        }
    )
}

private fun startTileDrag(
    key: Any,
    offset: Offset,
    label: String,
    dragAndDropState: DragAndDropState,
    itemBounds: Rect,
    itemGraphicsLayer: GraphicsLayer
) {
    val clipData = ClipData.newPlainText(label, label)

    dragAndDropState.startDrag(
        key = key,
        data = DragAndDropTransferData(
            clipData = clipData,
            localState = label,
            flags = 0
        ),
        dragItemLocalTouchOffset = offset,
        localBounds = itemBounds,
        itemGraphicsLayer = itemGraphicsLayer
    )
}

@Composable
private fun Modifier.dragShadowSource(
    key: Any,
    itemGraphicsLayer: GraphicsLayer,
    dragAndDropState: DragAndDropState,
    onBoundsChanged: (Rect) -> Unit
): Modifier {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var needsShadowCapture by remember { mutableStateOf(false) }

    return this
        .onGloballyPositioned { coordinates ->
            onBoundsChanged(coordinates.boundsInParent())
        }
        .graphicsLayer()
        .pointerInput(key) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                needsShadowCapture = true
            }
        }
        .drawWithContent {
            if (dragAndDropState.dragItemKey != key) {
                drawContent()
            }
            if (needsShadowCapture && size.width > 0 && size.height > 0) {
                itemGraphicsLayer.record(size) {
                    this@drawWithContent.drawContent()
                }
                needsShadowCapture = false
            }
        }
        .onSizeChanged { size = it; needsShadowCapture = true }
}