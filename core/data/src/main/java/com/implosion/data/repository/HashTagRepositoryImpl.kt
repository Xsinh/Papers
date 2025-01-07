package com.implosion.data.repository

import com.implosion.data.database.NotesDatabase
import com.implosion.data.database.entity.note.NoteTagCrossRef
import com.implosion.data.database.entity.tag.TagEntity
import com.implosion.data.database.entity.tag.mapper.toDomain
import com.implosion.domain.model.TagModel
import com.implosion.domain.repository.HashTagRepository

class HashTagRepositoryImpl(
    private val database: NotesDatabase
) : HashTagRepository {

    override suspend fun addTagToNote(noteId: Int, tagName: String) {

        val existingTag = getTagByName(tagName)
        val tagId = existingTag?.id ?: createNewTag(tagName).toInt()

        // Create and insert the cross-reference
        val crossRef = NoteTagCrossRef(noteId = noteId, tagId = tagId)
        database.noteTagCrossRefDao().insert(crossRef)
    }

    override suspend fun getAllTagsFromNote(noteId: Int?): List<TagModel> {
        return if (noteId != null) {
            database.noteTagCrossRefDao()
                .getTagsForNoteId(noteId = noteId).tags
                .map { it.toDomain() }
        } else {
            emptyList()
        }
    }

    private suspend fun getTagByName(tagName: String): TagEntity? {
        return database.tagDao().getTagByName(tagName)
    }

    private suspend fun createNewTag(tagName: String): Long {
        val newTag = TagEntity(name = tagName)
        return database.tagDao().insert(newTag)
    }
}