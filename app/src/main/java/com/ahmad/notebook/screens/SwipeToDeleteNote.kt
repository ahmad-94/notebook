package com.ahmad.notebook.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ahmad.notebook.data.Note

@Composable
fun SwipeToDeleteNote(
    note: Note,
    onDelete: (Note) -> Unit,
    content: @Composable (Note) -> Unit,
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                onDelete(note)
                true
            } else {
                false
            }
        },
        positionalThreshold = {totalDistance -> totalDistance * 0.6f}
    )

    SwipeToDismissBox(
        modifier = Modifier,
        state = dismissState,
        backgroundContent = {
            if (
                dismissState.dismissDirection.name == SwipeToDismissBoxValue.StartToEnd.name

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                    ) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
        },
        enableDismissFromEndToStart = false
    ) {
        content(note)
    }
}