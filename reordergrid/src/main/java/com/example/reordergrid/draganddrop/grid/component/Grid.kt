package com.example.reordergrid.draganddrop.grid.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.reordergrid.draganddrop.grid.state.GridState
import com.example.reordergrid.draganddrop.grid.state.rememberGridState

/**
 * Lays [items] out on a fixed [columns] x [rows] grid, animating each item to its new cell
 * whenever its index within [items] changes.
 *
 * @param items the items to display, in the order they should occupy the grid's cell.
 * @param columns number of columns in the grid.
 * @param rows number of rows in the grid.
 * @param state the [GridState] backing this grid; create one with [rememberGridState].
 * @param itemKey returns a stable, unique key for an item, used to track identity across
 * recompositions.
 * @param content the composable content for a single item's tile.
 */
@Composable
fun <T> Grid(
    modifier: Modifier,
    items: List<T>,
    columns: Int,
    rows: Int,
    state: GridState,
    itemKey: (T) -> Any,
    content: @Composable GridScope.(T) -> Unit
) {
    val scope = remember(state, columns, rows) {
        GridScope(state = state, columns = columns, rows = rows)
    }

    Box(modifier = modifier, contentAlignment = Alignment.TopStart) {
        items.forEachIndexed { index, item ->
            val retrievedKey = itemKey(item)
            key(retrievedKey) {
                Box(
                    with(scope) {
                        Modifier.animateGridCell(key = retrievedKey, index = index)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    scope.content(item)
                }
            }
        }
    }
}