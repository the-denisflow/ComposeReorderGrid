package io.github.thedenisflow.reordergrid.draganddrop.grid.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import io.github.thedenisflow.reordergrid.draganddrop.grid.component.Grid

@Composable
fun rememberGridState(): GridState = remember { GridState() }

/**
 * Tracks the current on-screen [Rect] of every tile in a [Grid], keyed by the same key passed to
 * [Grid]'s itemkey. [Grid] itself doesn't need this to draw or amimate tiles - it's here so a
 * drag-and-drap layer can answer "which tile is currently under the pointer?" by comparing a touch
 * position against these bounds, instead of recomputing grid geometry by hand.
 */
class GridState internal constructor() {
    private val cellBounds = mutableStateMapOf<Any, Rect>()

    /**
     * Returns the cell whose bounds contains [position], or null if [position]
     * doesn't fall inside any cell
     */
    fun findKeyAt(position: Offset): Any? = cellBounds.entries.firstOrNull { (_, bounds) ->
        bounds.contains(position)
    }?.key

    internal fun setCellBounds(key: Any, bounds: Rect) {
        cellBounds[key] = bounds
    }

    internal fun removeCellBounds(key: Any) {
        cellBounds.remove(key)
    }
}