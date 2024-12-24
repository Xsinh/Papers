package com.implosion.papers.data.database.entity.folder

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Structure of folder
 * @param name name of folder
 * @param parentId link to the parent folder, if any, to support subfolders.
 * Can be NULL for root folders
 * @param createdAt time the note was created in Unix timestamp format
 */
@Entity(tableName = "folder_table")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val parentId: Int?,
    val createdAt: Long
)
