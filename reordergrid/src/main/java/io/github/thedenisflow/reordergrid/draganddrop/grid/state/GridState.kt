package io.github.thedenisflow.reordergrid.draganddrop.grid.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

@Composable
fun rememberGridState(): GridState = remember { GridState() }

/**
 * Tracks the current on-screen [Rect] of every tile in a [io.github.thedenisflow.reordergrid.draganddrop.grid.component.Grid], keyed by the same key passed to
 * [io.github.thedenisflow.reordergrid.draganddrop.grid.component.Grid]'s itemKey. [io.github.thedenisflow.reordergrid.draganddrop.grid.component.Grid] itself doesn't need this to draw or animate tiles - it's here so a
 * drag-and-drop layer can answer "which tile is currently under the pointer?" by comparing a touch
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
        val overlappingKey = cellBounds.entries.firstOrNull { (otherKey, otherBounds) ->
            otherKey != key && otherBounds.overlaps(bounds)
        }?.key
        require(overlappingKey == null) {
            "Cell bounds for $key ($bounds) overlap existing cell $overlappingKey's bounds"
        }

        cellBounds[key] = bounds
    }

    internal fun removeCellBounds(key: Any) {
        cellBounds.remove(key)
    }
}