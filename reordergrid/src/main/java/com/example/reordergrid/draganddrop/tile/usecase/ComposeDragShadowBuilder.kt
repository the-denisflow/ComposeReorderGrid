package com.example.reordergrid.draganddrop.tile.usecase

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

/**
 * Custom DragShadowbuilder that renders Compose as a drag shadow.
 *
 * This class bridges Android's View-based drag and drop system with a Compose tile's content,
 * rendered from [graphicsLayer]. [graphicsLayer] must belong to an owner that is never disposed for
 * the duration of the drag (see [com.example.composereordergrid.presentation.draganddrop.tile.state.DragAndDropState.shadowLayer]) -
 * otherwise Compose may release and recycle it for a different composable while the drag is still
 * in progress, which would make the shadow blank or show the wrong tile.
 *
 * @param graphicsLayer The layer containing the dragged tile's content to display as the drag shadow.
 * @param density The screen density for converting pixels and dp.
 * @param layoutDirection The layout direction of the content being rendered.
 * @param touchPosition The position where the user touched the item.
 * @param size The size of the content being rendered.
 */
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
            size = size,
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
