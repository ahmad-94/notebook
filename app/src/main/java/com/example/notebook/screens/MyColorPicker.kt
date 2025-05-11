package com.example.notebook.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyColorPicker(selectedColor: Color, onSelectedColor: (Color) -> Unit) {

    val colorsList = listOf(

        Color(0xFFFF5722), // Deep Orange
        Color(0xFF4CAF50), // Vibrant Green
        Color(0xFF03A9F4), // Bright Blue
        Color(0xFFFFC107), // Bold Amber
        Color(0xFF9C27B0), // Strong Purple
        Color(0xFF00BCD4), // Cyan
        Color(0xFFFFEB3B), // Bright Yellow
        Color(0xFFF44336), // Red
        Color(0xFF3F51B5), // Indigo
        Color(0xFF009688), // Teal



    )

    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        items(colorsList) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(it)
                    .border(
                        width = if (selectedColor == it) 4.dp else 0.dp,
                        color = if (selectedColor == it) Color.Black else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        onSelectedColor(it)
                    }
            )
        }
    }
}