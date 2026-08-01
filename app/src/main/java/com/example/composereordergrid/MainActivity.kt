package com.example.composereordergrid

import androidx.compose.ui.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composereordergrid.data.local.LocalData
import com.example.composereordergrid.data.model.DemoTile
import com.example.composereordergrid.presentation.draganddrop.grid.component.Grid
import com.example.composereordergrid.ui.theme.ComposeReorderGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeReorderGridTheme {
            val tiles: List<DemoTile> = remember { LocalData.tiles.toMutableStateList() }

                Grid(
                    modifier = Modifier.fillMaxSize(),
                    items = tiles,
                    columns = 3,
                    rows =  2,
                    itemKey = { tile -> tile.id}
                ) {
                    ScreenTile(label = it.label, color = it.color )
                }
            }
        }
    }
}


@Composable
fun ScreenTile(
    label: String,
    color: Color
) {
    Box(
        modifier = Modifier.fillMaxSize().background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(label)
    }
}