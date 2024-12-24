package com.implosion.papers.data.repository

import com.implosion.papers.data.database.NotesDatabase
import com.implosion.papers.data.database.entity.note.toDTO
import com.implosion.papers.domain.model.NoteModel
import com.implosion.papers.domain.model.toDomain
import com.implosion.papers.domain.repository.NoteRepository

class NoteRepositoryImpl(
    private val database: NotesDatabase
) : NoteRepository {

    override suspend fun createNote(note: NoteModel) {
        database.noteDao().insert(note = note.toDTO())
    }

    override suspend fun getAllNotes(): List<NoteModel> {
        return database.noteDao().getAllNotes()
            .map { noteEntity ->
                noteEntity.toDomain()
            }
    }
}