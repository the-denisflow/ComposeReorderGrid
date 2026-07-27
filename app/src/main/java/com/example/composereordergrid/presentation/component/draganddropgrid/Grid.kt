package com.example.composereordergrid.presentation.component.draganddropgrid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Lays [items] out on a fixed [columns] x [rows] grid, animating each item to its new cell
 * whenever its index within [items] changes.
 *
 * @param items the items to display, in the order they should occupythe grid's cell.
 * @param columns number of columns in the grid.
 * @param rows number of rows in the grid.
 * @param state the [GridState] backing this grid; create one with [rememberGridState].
 * Exposes each item's current on-screen bounds via [GridState.getCells].
 * @param itemKey returns a stable, unique key for an item, used to tracj identity across
 * recompositions.
 * @param content the composable content for a single item's tile.
 */
@Composable
fun <T> Grid(
    modifier: Modifier,
    items: List<T>,
    columns: Int,
    rows: Int,
    state: GridState = rememberGridState(),
    itemKey: (T) -> Any,
    content: @Composable GridScope.(T) -> Unit
) {
    val scope = remember(state, columns, rows) {
        GridScope(state = state, columns = columns, rows = rows)
    }

    Box(modifier = modifier, contentAlignment = Alignment.TopCenter) {
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