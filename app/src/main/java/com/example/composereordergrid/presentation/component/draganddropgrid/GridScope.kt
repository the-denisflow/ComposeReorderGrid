package com.example.composereordergrid.presentation.component.draganddropgrid

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

/**
 * Receiver scope for [Grid]'s content lambda. Its only job is [animatedGridCell]: the
 * Modifier.layout math that places a tile in its (column, row) cell and animates it there
 * when its index changes.
 */
class GridScope internal constructor(
    private val state: GridState,
    private val columns: Int,
    private val rows: Int
) {

    @Composable
    internal fun Modifier.animateGridCell(key: Any, index: Int): Modifier {
       DisposableEffect(key) {
           state.removeCellBounds(key)
       }

        val animatedOffset by animateOffsetAsState(
            targetValue = Offset(
                (index % columns).toFloat(),
                (index / columns).toFloat()
            ),
            label = "gridCellOffset"
        )

        return this.layout {  measurable, constraints ->
            val cellWidth = constraints.maxWidth / columns
            val cellHeight = constraints.maxHeight / rows
            val placeable = measurable.measure(Constraints.fixed(cellWidth, cellHeight))
            layout(cellWidth, cellHeight) {
                placeable.place(
                    x = (animatedOffset.x * cellWidth).roundToInt(),
                    y = (animatedOffset.y * cellHeight).roundToInt()
                )
            }
        }.onGloballyPositioned {
            coordinates -> state.setCellBounds(key, coordinates.boundsInParent())
        }
    }
}