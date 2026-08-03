package io.github.thedenisflow.reordergrid.draganddrop

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Hosts the plain Android View that the drag and drop needs, since [android.view.View.startDragAndDrop]
 * [android.view.View.setOnDragListener] and [android.view.View.updateDragShadow] are only available
 * on [View], not on composables.
 *
 * Create an invisible, full-size [View] and stores it on [dragAndDropState] so it can be used as
 * the drag listener and drag shadow host, then draws [content] on top of it.
 *
 * @param dragAndDropState The state that will drive drag and drop for the [content] below.
 * @param content The actual UI (e.g. the grid) drawn on top of the hosting view.
 */
@Composable
fun DraggableArea(
    dragAndDropState: io.github.thedenisflow.reordergrid.draganddrop.tile.state.DragAndDropState,
    content: @Composable () -> Unit
) {
    AndroidView(
        factory = {
            context ->
            View(context).apply {
                dragAndDropState.localView = this
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    content()
}