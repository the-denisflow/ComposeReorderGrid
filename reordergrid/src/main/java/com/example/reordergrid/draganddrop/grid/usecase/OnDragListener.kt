package com.example.reordergrid.draganddrop.grid.usecase

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList

class OnDragListener<T> {
    private val CLASS_NAME = "OnDragListener"

    fun onDrag(fromKey: Any, toKey: Any, tiles: SnapshotStateList<T>, identifier: (T) -> Any) {
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


