package com.ahmad.notebook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmad.notebook.data.Note
import com.ahmad.notebook.repository.NotesRepository
import kotlinx.coroutines.launch

class NoteViewModel(private  val repository: NotesRepository): ViewModel() {


    val getAllNotes: LiveData<List<Note>> = repository.allNotes

    fun insertNote(note: Note, onInserted: (Int) -> Unit) = viewModelScope.launch {
        val id = repository.insertNote(note)
        onInserted(id)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}