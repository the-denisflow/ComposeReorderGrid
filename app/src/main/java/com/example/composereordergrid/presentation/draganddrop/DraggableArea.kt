package com.example.composereordergrid.presentation.draganddrop

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.composereordergrid.presentation.draganddrop.tile.state.DragAndDropState

@Composable
fun DraggableArea(
    dragAndDropState: DragAndDropState,
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