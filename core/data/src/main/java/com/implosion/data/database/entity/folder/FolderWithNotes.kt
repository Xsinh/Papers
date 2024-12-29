package com.implosion.data.database.entity.folder

import androidx.room.Embedded
import androidx.room.Relation
import com.implosion.data.database.entity.note.NoteEntity

data class FolderWithNotes(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val notes: List<NoteEntity>
)
