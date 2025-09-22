package com.implosion.domain.repository.papers

import com.implosion.domain.model.NoteModel

interface SearchRepository {

    suspend fun search(query: String): List<NoteModel>
}