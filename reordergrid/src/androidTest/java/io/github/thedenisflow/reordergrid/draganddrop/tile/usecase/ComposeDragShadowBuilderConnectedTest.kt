package io.github.thedenisflow.reordergrid.draganddrop.tile.usecase

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.renderscript.Sampler
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.IntSize
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * [ComposeDragShadowBuilder] needs a real [androidx.compose.ui.graphics.layer.GraphicsLayer],
 * which can't be constructed outside a live composition - that's why this is a connected test
 * rather than a JVM unit test.
 */
class ComposeDragShadowBuilderConnectedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var shadowBuilder: ComposeDragShadowBuilder

    private val touchPositionUnderTest = Offset(x = 30f, y = 40f)
    private val sizeUnderTest = Size(width = 100f, height = 200f)
    private val shadowContentColor = Color.Red
    private val shadowSizePx = IntSize(sizeUnderTest.width.toInt(), sizeUnderTest.height.toInt())

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val graphicsLayer = rememberGraphicsLayer()
            val density = LocalDensity.current
            val layoutDirection = LocalLayoutDirection.current

            LaunchedEffect(Unit) {
                graphicsLayer.record(density, layoutDirection, shadowSizePx) {
                    drawRect(color = shadowContentColor)
                }
            }

            shadowBuilder = ComposeDragShadowBuilder(
                density = density,
                graphicsLayer = graphicsLayer,
                layoutDirection = layoutDirection,
                touchPosition = touchPositionUnderTest,
                size = sizeUnderTest
            )
        }
    }

    @Test
    fun onProvideShadowMetrics_reportsSizeAndTouchPointInRealPixels() {
        val outShadowSize = Point()
        val outShadowTouchPoint = Point()

        shadowBuilder.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)

        assertEquals(sizeUnderTest.toPoint(), outShadowSize)
        assertEquals(touchPositionUnderTest.toPoint(), outShadowTouchPoint)
    }

    @Test
    fun onDrawShadow_rendersGraphicsLayerContentOntoCanvas() {
        composeTestRule.waitForIdle()

        // ARGB_8888 keeps this a software bitmap, since getPixel() throws on hardware bitmaps.
        val bitmap =
            Bitmap.createBitmap(
                Bitmap.createBitmap(shadowSizePx.width, shadowSizePx.height, Bitmap.Config.ARGB_8888)
            )
        val canvas = Canvas(bitmap)

        shadowBuilder.onDrawShadow(canvas)

        val bitmapMiddlePoint = Point(shadowSizePx.width / 2, shadowSizePx.height / 2)
        assertEquals(
            shadowContentColor.toArgb(),
            bitmap.getPixel(bitmapMiddlePoint.x, bitmapMiddlePoint.y)
        )
    }

    private fun Offset.toPoint(): Point = Point(this.x.toInt(), this.y.toInt())
    private fun Size.toPoint(): Point = Point(this.width.toInt(), this.height.toInt())
}
