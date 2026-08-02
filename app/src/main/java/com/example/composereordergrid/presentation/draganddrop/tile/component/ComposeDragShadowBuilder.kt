package com.example.composereordergrid.presentation.draganddrop.tile.component

import android.graphics.Canvas
import android.graphics.Point
import android.view.View
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize

class ComposeDragShadowBuilder internal constructor(
    private val graphicsLayer: GraphicsLayer,
    private val density: Density,
    private val layoutDirection: LayoutDirection,
    private val touchPosition: Offset,
    private val size: Size
) : View.DragShadowBuilder() {
    override fun onDrawShadow(canvas: Canvas) {
        CanvasDrawScope().draw(
            density = density,
            size = graphicsLayer.size.toSize(),
            layoutDirection = layoutDirection,
            canvas = androidx.compose.ui.graphics.Canvas(canvas)
        ) {
            drawLayer(graphicsLayer)
        }
    }

    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        with(density) {
            outShadowSize?.set(
                size.width.toDp().roundToPx(),
                size.height.toDp().roundToPx()
            )

            outShadowTouchPoint?.set(
                touchPosition.x.toDp().roundToPx(),
                touchPosition.y.toDp().roundToPx()
            )
        }
    }
}