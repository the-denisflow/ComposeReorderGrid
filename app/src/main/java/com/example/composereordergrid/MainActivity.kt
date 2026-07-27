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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composereordergrid.presentation.component.draganddropgrid.Grid
import com.example.composereordergrid.ui.theme.ComposeReorderGridTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeReorderGridTheme {
            val tiles =
                listOf(
                    DemoTile(0, "A", Color(0xFFEF5350)),
                    DemoTile(1, "B", Color(0xFF42A5F5)),
                    DemoTile(2, "C", Color(0xFF66BB6A)),
                    DemoTile(3, "D", Color(0xFFFFCA28)),
                    DemoTile(4, "E", Color(0xFFAB47BC)),
                    DemoTile(5, "F", Color(0xFF26A69A))
                )
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

private data class DemoTile(val id: Int, val label: String, val color: Color)

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