package com.implosion.papers.domain.repository

import com.implosion.papers.domain.model.NoteModel

interface NoteRepository {

    suspend fun createNote(note: NoteModel)

    suspend fun getAllNotes(): List<NoteModel>

    suspend fun getNote(id: Int): NoteModel
}