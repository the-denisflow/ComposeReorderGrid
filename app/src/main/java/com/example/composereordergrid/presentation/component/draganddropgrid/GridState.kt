package com.example.composereordergrid.presentation.component.draganddropgrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.DisposableEffectResult
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect

@Composable
fun rememberGridState(): GridState = remember { GridState() }

/**
 * Tracks the current on-screen [Rect] of every tile in a [Grid], keyed by the same key passed to
 * [Grid]'s itemkey. [Grid] itself doesn't meed thos tp draw or amimate tiles - it's jere sp a future
 * drag-and-drap layer can answer "which tile is currently under the pointer?" by comparing a touch
 * position against these bounds, instead of recomputing grid geometry by hand.
 */
class GridState internal constructor() {
    private val cellBounds = mutableStateMapOf<Any, Rect>()

    /** Snapshot of every currently-composed cell's bounds, for hit-testing a drag pointer againt. */
    fun getCells(): Map<Any, Rect> = cellBounds

    internal fun setCellBounds(key: Any, bounds: Rect) {
        cellBounds[key] = bounds
    }

    internal fun removeCellBounds(key: Any): DisposableEffectResult {
        cellBounds.remove(key)
    }
}