package com.example.composereordergrid

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.composereordergrid.data.local.LocalData
import com.example.composereordergrid.data.model.DemoTile
import com.example.composereordergrid.presentation.draganddrop.DraggableArea
import com.example.composereordergrid.presentation.draganddrop.grid.component.Grid
import com.example.composereordergrid.presentation.draganddrop.grid.state.GridState
import com.example.composereordergrid.presentation.draganddrop.grid.state.rememberGridState
import com.example.composereordergrid.presentation.draganddrop.grid.usecase.OnDragListener
import com.example.composereordergrid.presentation.draganddrop.pagedgrid.component.PagedGrid
import com.example.composereordergrid.presentation.draganddrop.pagedgrid.usecase.rememberPageFlipState
import com.example.composereordergrid.presentation.draganddrop.tile.component.DraggableItemContainer
import com.example.composereordergrid.presentation.draganddrop.tile.state.rememberDragAndDropState
import com.example.composereordergrid.ui.theme.ComposeReorderGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeReorderGridTheme {
                val columns = 4
                val rows = 8
                val itemsPerPage = columns * rows
                val tiles: SnapshotStateList<DemoTile> =
                    remember { LocalData.tiles.toMutableStateList() }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PagedGrid(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        rows = rows,
                        columns = columns,
                        tiles = tiles,
                        tileKeyIdentifier = { tile -> tile.id },
                        clipDataText = { tile -> tile.label }
                    ) { tile ->
                        SampleTile(
                            label = tile.label,
                            color = tile.color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SampleTile(
    label: String,
    color: Color
) {
    Box(modifier = Modifier.size(90.dp).background(color), contentAlignment = Alignment.Center) {
        Text(label)
    }
}
