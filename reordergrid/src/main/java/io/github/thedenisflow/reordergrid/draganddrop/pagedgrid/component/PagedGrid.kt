package io.github.thedenisflow.reordergrid.draganddrop.pagedgrid.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import io.github.thedenisflow.reordergrid.draganddrop.DraggableArea
import io.github.thedenisflow.reordergrid.draganddrop.grid.component.Grid
import io.github.thedenisflow.reordergrid.draganddrop.grid.state.rememberGridState
import io.github.thedenisflow.reordergrid.draganddrop.grid.usecase.OnDragListener
import io.github.thedenisflow.reordergrid.draganddrop.pagedgrid.usecase.rememberPageFlipState
import io.github.thedenisflow.reordergrid.draganddrop.tile.component.DraggableItemContainer
import io.github.thedenisflow.reordergrid.draganddrop.tile.state.rememberDragAndDropState

@Composable
fun <T> PagedGrid(
    modifier: Modifier,
    rows: Int,
    columns: Int,
    tiles: SnapshotStateList<T>,
    tileKeyIdentifier: (T) -> Any,
    clipDataText: (T) -> String,
    tileContent: @Composable (T) -> Unit,
    tileShadow: (@Composable (T) -> Unit)? = null
) {
    val itemsPerPage = rows * columns

    val gridState = rememberGridState()
    val pagerState = rememberPagerState(
        pageCount = { (tiles.size + itemsPerPage - 1) / itemsPerPage }
    )
    val pageFlipState = rememberPageFlipState(pagerState = pagerState)
    val dragAndDropState = rememberDragAndDropState(
        gridState = gridState,
        pageFlipState = pageFlipState,
        onMove = { fromKey, toKey -> OnDragListener<T>().onDrag(
            fromKey = fromKey,
            toKey = toKey,
            tiles =  tiles,
            identifier = { tileKeyIdentifier(it)}
        ) }
    )

    DraggableArea(dragAndDropState) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
        ) { page ->
            val pagesTiles = tiles.drop(page * itemsPerPage).take(itemsPerPage)
            Grid(
                modifier = Modifier.fillMaxSize(),
                items = pagesTiles,
                columns = columns,
                rows = rows,
                state = gridState,
                itemKey = { tile -> tileKeyIdentifier (tile) }
            ) {tile ->
                DraggableItemContainer(
                    key = tileKeyIdentifier(tile),
                    dragAndDropState = dragAndDropState,
                    clipDataText = clipDataText(tile),
                    tileContent = { tileContent(tile) },
                    tileShadow = tileShadow?.let { shadow -> { shadow(tile) } }
                )
            }
        }
    }
}
