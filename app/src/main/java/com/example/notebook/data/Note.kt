package com.example.notebook.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String ,
    var description: String ,
    var color: Int ,
    var alertDateTime: LocalDateTime? = null
)
