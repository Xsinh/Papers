package com.implosion.data.database.entity.note.mapper

import com.implosion.data.database.entity.note.NoteEntity
import com.implosion.domain.model.NoteModel


fun NoteEntity.toDomain(): NoteModel = NoteModel(
    noteId = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
