package com.implosion.papers.data.database.entity.note

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Structure of note
 * @param title - title of note
 * @param content - note's content
 * @param createdAt - time the note was created in Unix timestamp format
 * @param updatedAt - time the note was last updated
 * @param folderId - link to the folder in which the note is located
 * @param isTrashed - flag to move a note to the trash (for future storage options for deleted notes)
 */
@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val folderId: Int? = null, // Nullable для корневых заметок
    val isTrashed: Boolean = false,
)
