package com.example.composereordergrid.data.local

import androidx.compose.ui.graphics.Color
import com.example.composereordergrid.data.model.DemoTile

private val palette = listOf(
    Color(0xFFEF5350),
    Color(0xFF42A5F5),
    Color(0xFF66BB6A),
    Color(0xFFFFCA28),
    Color(0xFFAB47BC),
    Color(0xFF26A69A),
    Color(0xFFFF7043),
    Color(0xFF8D6E63)
)

object LocalData {
    val tiles: List<DemoTile> = (0 until 40).map { index ->
        DemoTile(id = index, label = "T$index", color = palette[index % palette.size])
    }
}