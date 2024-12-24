package com.implosion.papers.data.database.entity.note

import com.implosion.papers.domain.model.NoteModel

fun NoteModel.toDTO(): NoteEntity = NoteEntity(
    title = title.orEmpty(),
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)