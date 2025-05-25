package com.ahmad.notebook.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ahmad.notebook.data.Note
import com.ahmad.notebook.roomdb.NoteDao
import com.ahmad.notebook.roomdb.NotesDB
import com.ahmad.notebook.screens.scheduleAlarm
import java.time.LocalDateTime
import java.time.ZoneId


class NotesRepository(private val noteDao: NoteDao) {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insertNote(note: Note): Int { return noteDao.insertNote(note).toInt() }

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)


    object AlarmScheduler {
        fun rescheduleAllAlarms(context: Context) {
            val db = NotesDB.getInstance(context)
            val notesWithAlarms = db.noteDao.getNotesWithAlarms()

            for (note in notesWithAlarms) {
                val dateTime = note.alertDateTime
                val millis = dateTime
                    ?.atZone(ZoneId.systemDefault())
                    ?.toInstant()
                    ?.toEpochMilli()

                if (millis != null && millis > System.currentTimeMillis()) {
                    scheduleAlarm(
                        context = context,
                        noteId = note.id,
                        title = note.title,
                        description = note.description,
                        dateTime = dateTime
                    )
                }
            }
        }
    }



}