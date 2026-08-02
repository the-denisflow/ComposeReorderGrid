package com.example.composereordergrid.presentation.draganddrop.tile.component

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.sourceInformationMarkerEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropStartTransferScope
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.composereordergrid.presentation.draganddrop.tile.state.DragAndDropState

private val tileSize = 80.dp

@Composable
fun DraggableItemContainer (
    key: Any,
    label: String,
    color: Color,
    dragAndDropState: DragAndDropState
) {
    val itemGraphicsLayer = rememberGraphicsLayer()
    var itemBouns by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect (Unit) {
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
            .background(color)
            .size(tileSize)
            .detectDragGesture(
                key = key,
                dragAndDropState = dragAndDropState,
                itemBounds = itemBouns,
                itemGraphicsLayer = itemGraphicsLayer,
                label
            )
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(label)
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
    detectDragGesturesAfterLongPress (
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
        onDrag = {change, _ ->
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
)  {
    val clipData = ClipData.newPlainText(label, label)

    dragAndDropState.startDrag(
        key = key,
        data = DragAndDropTransferData(
            clipData = clipData,
            localState = label,
            flags =0

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

    return this
        .onGloballyPositioned { coordinates ->
            onBoundsChanged(coordinates.boundsInParent())
        }
        .graphicsLayer()
        .drawWithContent {
            if (dragAndDropState.dragItemKey != key) {
                drawContent()
            }
            if (size.width > 0 && size.height > 0) {
                itemGraphicsLayer.record(size) {
                    this@drawWithContent.drawContent()
                }
            }
            dragAndDropState.let { state ->
                state.localView.updateDragShadow(
                    state.dragShadowBuilder
                )
            }
        }
        .onSizeChanged { size = it }
}