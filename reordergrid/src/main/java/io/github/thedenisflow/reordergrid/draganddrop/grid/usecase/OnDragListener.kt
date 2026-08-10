package io.github.thedenisflow.reordergrid.draganddrop.grid.usecase

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.github.thedenisflow.reordergrid.draganddrop.grid.contract.DragAndDropListener

class OnDragListener<T> constructor (
    private val tiles: SnapshotStateList<T>,
    private val identifier: (T) -> Any
): DragAndDropListener{
    private val CLASS_NAME = "OnDragListener"

    private var lastDragItemKey: Any? = null
    private var lastDragOverItem: Any? = null

    override fun onMove(dragItemKey: Any, dragOverItemKey: Any?) {
        if(dragItemKey != lastDragItemKey){
            lastDragItemKey = dragItemKey
            lastDragOverItem = null
        }
        if(dragOverItemKey == lastDragOverItem) return

        lastDragOverItem = dragOverItemKey

        dragOverItemKey?.let {
            onDrag(
                dragItemKey,  dragOverItemKey
            )
        }
    }

    override fun onDragEnded() {
        lastDragItemKey = null
        lastDragOverItem = null
    }

    private fun onDrag(fromKey: Any, toKey: Any) {
        val fromIndex = tiles.indexOfFirst { identifier(it) == fromKey }
        val toIndex = tiles.indexOfFirst { identifier(it) == toKey }

        Log.i(CLASS_NAME, "onDrag from index: $fromIndex to index:$toIndex")

        if (fromIndex != -1 && toIndex != -1) {
            tiles.moveItem(fromIndex, toIndex)
        }
    }

    private fun SnapshotStateList<T>.moveItem(
        from: Int, to: Int) {
        if (from == to || from !in indices || to !in indices) return
        else {
            add(to, removeAt(from))
            Log.i(CLASS_NAME, "moveItem from: $from to: $to")
        }
    }

}


