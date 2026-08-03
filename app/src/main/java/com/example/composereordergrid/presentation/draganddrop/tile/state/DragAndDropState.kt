package com.example.composereordergrid.presentation.draganddrop.tile.state

import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.example.composereordergrid.presentation.draganddrop.grid.state.GridState
import com.example.composereordergrid.presentation.draganddrop.pagedgrid.usecase.PageFlipState
import com.example.composereordergrid.presentation.draganddrop.tile.usecase.ComposeDragShadowBuilder

@Composable
fun rememberDragAndDropState(
    gridState: GridState,
    onMove: (fromKey: Any, toKey: Any) -> Unit,
    pageFlipState: PageFlipState? = null,
    density: Density = LocalDensity.current,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current
): DragAndDropState = DragAndDropState(
    density = density,
    layoutDirection = layoutDirection,
    gridState = gridState,
    onMove = onMove,
    pageFlipState = pageFlipState,
    // Created here, above/outside any lazily-composed content (e.g. a pager), so it's never
    // disposed mid-drag - see the shadowLayer doc below for why that matters.
    shadowLayer = rememberGraphicsLayer()
)

/**
 * Bridges Compose's pointer-based drag gesture to the platform's native
 * [android.view.DragAndDropPermissions] flow, and drives grid reordering from it.
 *
 * A tile starts a native drag via [startDrag]. From then on, this class is registered as the
 * [View.OnDragListener] on [localView] (the [DraggableArea][com.example.composereordergrid.presentation.draganddrop.DraggableArea]'s
 * overlay view) and receives every [DragEvent] for the duration of the drag.
 */
class DragAndDropState internal constructor(
    private val density: Density,
    private val layoutDirection: LayoutDirection,
    private val gridState: GridState,
    private val onMove: (fromKey: Any, overKey: Any) -> Unit,
    private val pageFlipState: PageFlipState? = null,
    internal val shadowLayer: GraphicsLayer
) : View.OnDragListener {
    private val STATE_TAG = "DragAndDropState"

    var dragShadowBuilder: View.DragShadowBuilder by mutableStateOf(View.DragShadowBuilder())
        private set

    var dragItemKey: Any? by mutableStateOf(null)

    private var lastDragOverKey: Any? = null

    lateinit var localView: View

    /**
     * Starts a native drag-and-drop operation for the tile identified by [key].
     */
    fun startDrag(
        key: Any,
        data: DragAndDropTransferData,
        dragItemLocalTouchOffset: Offset = Offset.Zero,
        localBounds: Rect,
        itemGraphicsLayer: GraphicsLayer
    ) {
        require(::localView.isInitialized) {
            Log.w(STATE_TAG, "Local view is not initialized")
        }

        Log.i(STATE_TAG, "startDrag, local bounds: $localBounds")

        dragItemKey = key
        lastDragOverKey = null


        shadowLayer.record(
            density = density,
            layoutDirection = layoutDirection,
            size = itemGraphicsLayer.size
        ) {
            drawLayer(itemGraphicsLayer)
        }

        dragShadowBuilder = ComposeDragShadowBuilder(
            graphicsLayer = shadowLayer,
            density = density,
            layoutDirection = layoutDirection,
            touchPosition = dragItemLocalTouchOffset,
            size = localBounds.size
        )

        localView.startDragAndDrop(
            data.clipData,
            dragShadowBuilder,
            data.localState,
            data.flags
        )
    }

    /**
     * Handles drag events dispatched to [localView] for the duration of a drag.
     *
     * On [DragEvent.ACTION_DRAG_LOCATION], resolves the pointer position to a grid key via
     * [GridState.findKeyAt] and calls [onMove] the first time it resolves to a new cell other
     * than the dragged tile itself; also reports the position to [pageFlipState] so it can
     * auto-advance the page when the pointer sits near a screen edge. On
     * [DragEvent.ACTION_DRAG_ENDED], clears drag state.
     */
    override fun onDrag(p0: View?, p1: DragEvent?): Boolean {
        val event = p1 ?: return true
        when (event.action) {
            DragEvent.ACTION_DRAG_LOCATION -> {
                val fromKey = dragItemKey ?: return true
                val position = Offset(event.x, event.y)
                val overKey = gridState.findKeyAt(position)

                if (overKey != null && overKey != fromKey && overKey != lastDragOverKey) {
                    Log.i(STATE_TAG, "onDrag, move: $fromKey to $overKey")

                    onMove(fromKey, overKey)
                    lastDragOverKey = overKey
                } else if (overKey == null || overKey == fromKey) {
                    lastDragOverKey = null
                }

                pageFlipState?.onDrag(position, localView.width, density)
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                dragItemKey = null
                lastDragOverKey = null
                pageFlipState?.onDragEnded()
            }
        }
        return true
    }
}