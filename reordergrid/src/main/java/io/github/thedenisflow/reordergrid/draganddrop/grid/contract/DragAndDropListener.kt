package io.github.thedenisflow.reordergrid.draganddrop.grid.contract

import android.view.DragEvent
import io.github.thedenisflow.reordergrid.draganddrop.tile.state.DragAndDropState
import io.github.thedenisflow.reordergrid.draganddrop.tile.state.rememberDragAndDropState

/**
 * Receives grid-hover callbacks during a native[DragEvent]-based drag that [DragAndDropState]
 * drives. Inject an implementation via [rememberDragAndDropState] to react to the dragged
 * item movinf over a new cell.
 */
interface DragAndDropListener {
    /**
     * Invoked in [DragEvent.ACTION_DRAG_LOCATION] the first time the pointer resolves to a new
     * grid cell.
     * @param dragItemKey the key of the grid item currently being dragged.
     * @param dragOverItemKey the key of the grid item currently under the drag pointer, resolved
     * via [io.github.thedenisflow.reordergrid.draganddrop.grid.state.GridState.findKeyAt]
     */
    fun onMove(dragItemKey: Any, dragOverItemKey: Any?)

    /**
     * Invoked on [DragEvent.ACTION_DRAG_ENDED] to clear any per-gesture state (e.g. the last
     * seen drag/drag-over keys), so it doesn't leak into the next drag gesture.
     */
    fun onDragEnded()
}