package com.implosion.papers.domain.model

import com.implosion.papers.data.database.entity.note.NoteEntity

fun NoteEntity.toDomain(): NoteModel = NoteModel(
    noteId = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
