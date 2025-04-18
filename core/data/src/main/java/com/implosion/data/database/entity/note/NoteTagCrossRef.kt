package com.implosion.data.database.entity.note

import androidx.room.Entity


@Entity(primaryKeys = ["noteId", "tagId"])
data class NoteTagCrossRef(
    val noteId: Int,
    val tagId: Int
)
