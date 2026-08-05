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

        listenerUnderTest = OnDragListener()
    }

    @Test
    fun onDrag_whenDraggedIntoLaterKey_movesItemForward() {
        listenerUnderTest.onDrag(
            fromKey = 0,
            toKey = 1,
            tiles = tiles,
            identifier = identifier
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
    fun onDrag_whenDraggedIntoEarlyKey_movesItemBackward() {
        listenerUnderTest.onDrag(
            fromKey = 2,
            toKey = 0,
            tiles = tiles,
            identifier = identifier
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
    fun onDrag_whenFromKeyEqualsToKey_doesNothing() {
        listenerUnderTest.onDrag(
            fromKey = 1,
            toKey = 1,
            tiles = tiles,
            identifier = identifier
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
    fun onDrag_whenFromKeyIsNotFound_doesNothing() {
        listenerUnderTest.onDrag(
            fromKey = -1,
            toKey = 1,
            tiles = tiles,
            identifier = identifier
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
    fun onDrag_whenToKeyIsNotFound_doesNothing() {
        listenerUnderTest.onDrag(
            fromKey = 0,
            toKey = -1,
            tiles = tiles,
            identifier = identifier
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

    data class TileData(
        val label: String,
        val id: Int
    )
}
