package com.ahmad.notebook.repository

import androidx.lifecycle.LiveData
import com.ahmad.notebook.data.Note
import com.ahmad.notebook.roomdb.NoteDao


class NotesRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note): Int { return noteDao.insertNote(note).toInt() }

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

}