package com.example.composereordergrid.data.local

import androidx.compose.ui.graphics.Color
import com.example.composereordergrid.data.model.DemoTile

object LocalData {
    val tiles = listOf(
        DemoTile(0, "A", Color(0xFFEF5350)),
        DemoTile(1, "B", Color(0xFF42A5F5)),
        DemoTile(2, "C", Color(0xFF66BB6A)),
        DemoTile(3, "D", Color(0xFFFFCA28)),
        DemoTile(4, "E", Color(0xFFAB47BC)),
        DemoTile(5, "F", Color(0xFF26A69A))
    )
}