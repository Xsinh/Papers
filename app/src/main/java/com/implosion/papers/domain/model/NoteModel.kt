package com.implosion.papers.domain.model

data class NoteModel(
    val title: String? = null,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
)