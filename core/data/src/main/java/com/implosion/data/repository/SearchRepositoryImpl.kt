package com.implosion.data.repository

import com.implosion.data.database.NotesDatabase
import com.implosion.data.database.entity.note.mapper.toDomain
import com.implosion.domain.model.NoteModel
import com.implosion.domain.repository.papers.SearchRepository

class SearchRepositoryImpl(
    private val database: NotesDatabase
) : SearchRepository {

    override suspend fun search(query: String): List<NoteModel> {
        return if (query.startsWith("#")) {
            // Убираем символ `#` и передаём в поиск по тегам
            val tagName = query.substring(1)
            database.noteTagCrossRefDao()
                .searchTag(tagName).map { item ->
                    item.note.toDomain()
                }
        } else {
            // Поиск по тексту заметок
            database.noteDao()
                .searchNotesByText(query)
                .map { it.toDomain() }
        }
    }
}