package io.github.thedenisflow.reordergrid.draganddrop.grid.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GridStateTest {
    private lateinit var stateUnderTest: GridState

    @BeforeEach
    fun setUp() {
        stateUnderTest = GridState()
    }

    @Test
    fun findKeyAt_returnsKey_whenPositionIsInsideItsBounds() {
        stateUnderTest.setCellBounds(key = "a", bounds = Rect(left = 0f, top = 0f, right = 10f, bottom = 10f))

        assertEquals("a", stateUnderTest.findKeyAt(Offset(x = 5f, y = 5f)))
    }

    @Test
    fun findKeyAt_returnsNull_whenPositionIsOutsideAllBounds() {
        stateUnderTest.setCellBounds(key = "a", bounds = Rect(left = 0f, top = 0f, right = 10f, bottom = 10f))

        assertNull(stateUnderTest.findKeyAt(Offset(x = 50f, y = 50f)))
    }

    @Test
    fun findKeyAt_returnsNull_whenNoCellsAreRegistered() {
        assertNull(stateUnderTest.findKeyAt(Offset.Zero))
    }

    @Test
    fun findKeyAt_distinguishesBetweenAdjacentCells() {
        stateUnderTest.setCellBounds(key = "a", bounds = Rect(left = 0f, top = 0f, right = 10f, bottom = 10f))
        stateUnderTest.setCellBounds(key = "b", bounds = Rect(left = 10f, top = 0f, right = 20f, bottom = 10f))

        assertEquals("b", stateUnderTest.findKeyAt(Offset(x = 15f, y = 5f)))
    }

    @Test
    fun findKeyAt_returnsNull_afterItsCellBoundsAreRemoved() {
        stateUnderTest.setCellBounds(key = "a", bounds = Rect(left = 0f, top = 0f, right = 10f, bottom = 10f))

        stateUnderTest.removeCellBounds(key = "a")

        assertNull(stateUnderTest.findKeyAt(Offset(x = 5f, y = 5f)))
    }

    @Test
    fun setCellBounds_throws_whenBoundsOverlapAnExistingCell() {
        val existingKey = "a"
        stateUnderTest.setCellBounds(key = existingKey, bounds = Rect(left = 0f, top = 0f, right = 10f, bottom = 10f))

        val keyUnderTest = "b"
        val boundsUnderTest = Rect(left = 5f, top = 5f, right = 15f, bottom = 15f)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            stateUnderTest.setCellBounds(key = keyUnderTest, bounds = boundsUnderTest)
        }

        assertEquals(
            "Cell bounds for $keyUnderTest ($boundsUnderTest) overlap existing cell $existingKey's bounds",
            exception.message
        )
    }

    @Test
    fun setCellBounds_doesNotThrow_whenUpdatingTheSameKeysBounds() {
        stateUnderTest.setCellBounds(key = "a", bounds = Rect(left = 0f, top = 0f, right = 10f, bottom = 10f))

        stateUnderTest.setCellBounds(key = "a", bounds = Rect(left = 1f, top = 1f, right = 11f, bottom = 11f))

        assertEquals("a", stateUnderTest.findKeyAt(Offset(x = 6f, y = 6f)))
    }
}
