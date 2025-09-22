package com.implosion.data.repository

import com.implosion.data.database.NotesDatabase
import com.implosion.domain.model.NoteModel
import com.implosion.data.database.entity.note.mapper.toDTO
import com.implosion.data.database.entity.note.mapper.toDomain
import com.implosion.domain.repository.papers.NoteRepository

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

    override suspend fun getNote(id: Int): NoteModel {
        return database.noteDao().getNoteById(id).toDomain()
    }

    override suspend fun updateNote(note: NoteModel) {
        database.noteDao().update(note = note.toDTO())
    }

    override suspend fun deleteNoteById(id: Int) {
        database.noteDao().deleteById(noteId = id)
    }
}