package com.ahmad.notebook.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ahmad.notebook.data.Note


@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(note: Note): Long

    @Query("""
        SELECT * FROM notes_table
    """)
    fun getAllNotes(): LiveData<List<Note>>

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table WHERE alertDateTime IS NOT NULL")
    fun getNotesWithAlarms(): List<Note>

}