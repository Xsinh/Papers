package com.implosion.data.database.entity.note

import com.implosion.domain.model.NoteModel


fun NoteEntity.toDomain(): NoteModel = NoteModel(
    noteId = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
