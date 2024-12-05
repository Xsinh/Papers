package com.implosion.papers.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.implosion.papers.data.entity.NoteEntity.Companion.NOTE_TABLE_NAME

@Entity(tableName = NOTE_TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val folderId: Int? // Nullable для корневых заметок
){
    companion object{
        const val NOTE_TABLE_NAME = "notes_table"
    }
}
