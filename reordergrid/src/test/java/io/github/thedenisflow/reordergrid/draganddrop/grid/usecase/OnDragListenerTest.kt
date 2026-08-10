package io.github.thedenisflow.reordergrid.draganddrop.grid.usecase

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OnDragListenerTest {

    private lateinit var listenerUnderTest: OnDragListener<TileData>
    private lateinit var tiles: SnapshotStateList<TileData>
    private val identifier: (TileData) -> Int = { it.id }

    @BeforeEach
    fun setUp() {
        tiles = mutableStateListOf(
            TileData("Tile: 0", id = 0),
            TileData("Tile: 1", id = 1),
            TileData("Tile: 2", id = 2)
        )

        listenerUnderTest = OnDragListener(
            tiles = tiles,
            identifier = identifier
        )
    }

    @Test
    fun onMove_whenDraggedIntoLaterKey_movesItemForward() {
        listenerUnderTest.onMove(
            dragItemKey = 0,
            dragOverItemKey = 1
        )

        assertEquals(
            listOf(
                TileData("Tile: 1", id = 1),
                TileData("Tile: 0", id = 0),
                TileData("Tile: 2", id = 2)
            ),
            tiles,
        )
    }

    @Test
    fun onMove_whenDraggedIntoEarlyKey_movesItemBackward() {
        listenerUnderTest.onMove(
            dragItemKey = 2,
            dragOverItemKey = 0
        )

        assertEquals(
            listOf(
                TileData("Tile: 2", id = 2),
                TileData("Tile: 0", id = 0),
                TileData("Tile: 1", id = 1)
            ),
            tiles,
        )
    }

    @Test
    fun onMove_whenFromKeyEqualsToKey_doesNothing() {
        listenerUnderTest.onMove(
            dragItemKey = 1,
            dragOverItemKey = 1
        )

        assertEquals(
            listOf(
                TileData("Tile: 0", id = 0),
                TileData("Tile: 1", id = 1),
                TileData("Tile: 2", id = 2)
            ),
            tiles,
        )
    }

    @Test
    fun onMove_whenFromKeyIsNotFound_doesNothing() {
        listenerUnderTest.onMove(
            dragItemKey = -1,
            dragOverItemKey = 1
        )

        assertEquals(
            listOf(
                TileData("Tile: 0", id = 0),
                TileData("Tile: 1", id = 1),
                TileData("Tile: 2", id = 2)
            ),
            tiles
        )
    }

    @Test
    fun onMove_whenToKeyIsNotFound_doesNothing() {
        listenerUnderTest.onMove(
            dragItemKey = 0,
            dragOverItemKey = -1
        )

        assertEquals(
            listOf(
                TileData("Tile: 0", id = 0),
                TileData("Tile: 1", id = 1),
                TileData("Tile: 2", id = 2)
            ),
            tiles,
        )
    }

    @Test
    fun onMove_whenDragOverKeyRepeats_isDedupedAndDoesNotMoveAgain() {
        listenerUnderTest.onMove(dragItemKey = 0, dragOverItemKey = 1)
        listenerUnderTest.onMove(dragItemKey = 0, dragOverItemKey = 1)

        assertEquals(
            listOf(
                TileData("Tile: 1", id = 1),
                TileData("Tile: 0", id = 0),
                TileData("Tile: 2", id = 2)
            ),
            tiles,
        )
    }

    @Test
    fun onMove_afterOnDragEnded_sameItemAndDragOverKeyMovesAgain() {
        listenerUnderTest.onMove(dragItemKey = 0, dragOverItemKey = 1)
        listenerUnderTest.onDragEnded()

        // A new drag gesture for the same item, landing on the same drag-over key the previous
        // gesture ended on, must still trigger a move rather than being deduped against stale
        // state left over from the prior gesture.
        listenerUnderTest.onMove(dragItemKey = 0, dragOverItemKey = 1)

        assertEquals(
            listOf(
                TileData("Tile: 0", id = 0),
                TileData("Tile: 1", id = 1),
                TileData("Tile: 2", id = 2)
            ),
            tiles,
        )
    }

    data class TileData(
        val label: String,
        val id: Int
    )
}
