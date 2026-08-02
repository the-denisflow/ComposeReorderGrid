package com.example.composereordergrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import com.example.composereordergrid.data.local.LocalData
import com.example.composereordergrid.data.model.DemoTile
import com.example.composereordergrid.presentation.draganddrop.DraggableArea
import com.example.composereordergrid.presentation.draganddrop.grid.component.Grid
import com.example.composereordergrid.presentation.draganddrop.grid.state.rememberGridState
import com.example.composereordergrid.presentation.draganddrop.tile.component.DraggableItemContainer
import com.example.composereordergrid.presentation.draganddrop.tile.state.rememberDragAndDropState
import com.example.composereordergrid.ui.theme.ComposeReorderGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeReorderGridTheme {
                val gridState = rememberGridState()
                val dragAndDropState = rememberDragAndDropState(
                    gridState = gridState,
                    onMove = { fromKey, toKey -> }
                )
                val tiles: List<DemoTile> = remember { LocalData.tiles.toMutableStateList() }
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    DraggableArea(
                        dragAndDropState
                    ) {
                        Grid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            items = tiles,
                            columns = 4,
                            rows = 8,
                            itemKey = { tile -> tile.id }
                        ) {
                            DraggableItemContainer(
                                key = it.id,
                                label = it.label,
                                color = it.color,
                                dragAndDropState = dragAndDropState
                            )
                        }
                    }
                }
            }
        }
    }
}
