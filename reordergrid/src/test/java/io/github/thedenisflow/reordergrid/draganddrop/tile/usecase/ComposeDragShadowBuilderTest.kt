package io.github.thedenisflow.reordergrid.draganddrop.tile.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ComposeDragShadowBuilderTest {

    private val densityUnderTest = Density(density = 2f)

    @Test
    fun roundToShadowSize_roundsFractionalPixelsAndKeepsWidthHeightOrder() {
        val size = Size(width = 101.6f, height = 250.6f)

        val result = densityUnderTest.roundToShadowSize(size)

        assertEquals(IntSize(width = 102, height = 251), result)
    }

    @Test
    fun roundToShadowTouchPoint_roundsFractionalPixelsAndKeepsXYOrder() {
        val touchPosition = Offset(x = 30.9f, y = 45.9f)

        val result = densityUnderTest.roundToShadowTouchPoint(touchPosition)

        assertEquals(IntOffset(x = 31, y = 46), result)
    }
}