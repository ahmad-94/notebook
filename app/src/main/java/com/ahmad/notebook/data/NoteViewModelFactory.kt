package com.ahmad.notebook.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmad.notebook.repository.NotesRepository
import com.ahmad.notebook.viewmodel.NoteViewModel

class NoteViewModelFactory(private val repository: NotesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
