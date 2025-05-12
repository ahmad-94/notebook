package com.ahmad.notebook.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmad.notebook.data.Note

@Composable
fun DisplayNotesList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
    onNoteDelete: (Note) -> Unit,
) {

    LazyColumn (
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(notes, key = {it.id}) {
            SwipeToDeleteNote(it, {onNoteDelete(it)}) {
                DisplayNoteItem(it) {
                    onNoteClick(it)
                }
            }
        }
    }
}