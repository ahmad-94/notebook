package com.ahmad.notebook.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmad.notebook.data.Converters
import com.ahmad.notebook.data.Note


@Database(entities = [Note::class], version = 5)
@TypeConverters(Converters::class)
abstract class NotesDB: RoomDatabase(){
    abstract val noteDao: NoteDao

    companion object {

        @Volatile
        private var INSTANCE: NotesDB? = null

        fun getInstance(context: Context): NotesDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDB::class.java,
                        "notes_db"
                    )
                        .fallbackToDestructiveMigration(false)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}