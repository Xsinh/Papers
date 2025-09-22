package com.implosion.domain.repository.papers

import com.implosion.domain.model.TagModel

interface HashTagRepository {

    suspend fun addTagToNote(noteId: Int, tagName: String)

    suspend fun getAllTagsFromNote(noteId: Int?): List<TagModel>

    suspend fun deleteHashTag(hashTagId: Int, noteId: Int)
}