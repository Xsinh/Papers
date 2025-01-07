package com.implosion.domain.model

data class NoteModel(
    val noteId: Int? = null,
    val title: String? = null,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long,
    val hashTagList: List<TagModel> = emptyList<TagModel>()
)