package com.implosion.papers.presentation.interactor

import com.implosion.domain.model.NoteModel
import com.implosion.domain.repository.HashTagRepository
import com.implosion.domain.repository.NoteRepository
import com.implosion.domain.repository.SearchRepository

class NoteInteractorImpl(
    private val noteRepository: NoteRepository,
    private val hashTagRepository: HashTagRepository,
    private val searchRepository: SearchRepository
) : NoteInteractor {

    override suspend fun getNotesWithTags(): List<NoteModel> {
        val notes = noteRepository.getAllNotes()
        return notes.map { note ->
            val tags = hashTagRepository.getAllTagsFromNote(note.noteId)
            note.copy(hashTagList = tags)
        }
    }

    override suspend fun addHashTag(noteId: Int, tagName: String) {
        hashTagRepository.addTagToNote(noteId, tagName)
    }

    override suspend fun deleteHashTag(hashTagId: Int, noteId: Int) {
        hashTagRepository.deleteHashTag(hashTagId = hashTagId, noteId = noteId)
    }

    override suspend fun searchNotes(query: String): List<NoteModel> {
        val notes = searchRepository.search(query)
        return notes.map { note ->
            val tags = hashTagRepository.getAllTagsFromNote(note.noteId)
            note.copy(hashTagList = tags)
        }
    }

    override suspend fun deleteNote(noteId: Int) {
        noteRepository.deleteNoteById(noteId)
    }
}