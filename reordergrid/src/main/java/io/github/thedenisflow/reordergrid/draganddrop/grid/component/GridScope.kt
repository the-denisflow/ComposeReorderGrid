package io.github.thedenisflow.reordergrid.draganddrop.grid.component

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Constraints
import io.github.thedenisflow.reordergrid.draganddrop.grid.state.GridState
import kotlin.math.roundToInt

/**
 * Receiver scope for [Grid]'s content lambda. Its only job is [animateGridCell]: the
 * Modifier.layout math that places a tile in its (column, row) cell and animates it there
 * when its index changes.
 *
 * [cellWidth] and [cellHeight] (in px) must be measured once by the caller - e.g. via
 * `BoxWithConstraints` - covering the whole grid, and divided by the grid's columns/rows. They
 * can't be derived from the constraints an individual item receives at layout time, since that's
 * only reliable when every item's parent hands it the full grid's bounded constraints (as a
 * plain `Box` does). A lazy layout such as `LazyVerticalGrid` constrains an item to its own slot
 * and passes an unbounded constraint on the scroll axis, so dividing that by the row count would
 * try to build a [Constraints.fixed] out of an effectively infinite height.
 */
class GridScope internal constructor(
    private val state: GridState,
    private val columns: Int,
    private val cellWidth: Int,
    private val cellHeight: Int
) {
    private val animationDuration = 300
    private val gridCellLabel = "gridCellOffset"

    @Composable
    internal fun Modifier.animateGridCell(key: Any, index: Int): Modifier {
        DisposableEffect(key) {
           onDispose { state.removeCellBounds(key) }
        }

        val animatedOffset by animateOffsetAsState(
            targetValue = Offset(
                (index % columns).toFloat(),
                (index / columns).toFloat()
            ),
            animationSpec = tween(durationMillis = animationDuration),
            label = gridCellLabel
        )

        return this.layout { measurable, _ ->
            val placeable = measurable.measure(Constraints.fixed(cellWidth, cellHeight))
            layout(cellWidth, cellHeight) {
                placeable.place(
                    x = (animatedOffset.x * cellWidth).roundToInt(),
                    y = (animatedOffset.y * cellHeight).roundToInt()
                )
            }
        }.onGloballyPositioned {
            coordinates -> state.setCellBounds(key, coordinates.boundsInRoot())
        }
    }
}