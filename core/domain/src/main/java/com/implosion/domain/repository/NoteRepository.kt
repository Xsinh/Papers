package com.implosion.domain.repository

import com.implosion.domain.model.NoteModel

interface NoteRepository {

    suspend fun createNote(note: NoteModel)

    suspend fun getAllNotes(): List<NoteModel>

    suspend fun getNote(id: Int): NoteModel

    suspend fun updateNote(note: NoteModel)

    suspend fun deleteNoteById(id: Int)
}