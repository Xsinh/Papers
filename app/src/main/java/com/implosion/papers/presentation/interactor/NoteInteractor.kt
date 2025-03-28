package com.implosion.papers.presentation.interactor

import com.implosion.domain.model.NoteModel

interface NoteInteractor {

    suspend fun getNotesWithTags(): List<NoteModel>

    suspend fun addHashTag(noteId: Int, tagName: String)

    suspend fun deleteHashTag(hashTagId: Int, noteId: Int)

    suspend fun searchNotes(query: String): List<NoteModel>

    suspend fun deleteNote(noteId: Int)

    suspend fun markNoteItemAsComplete(noteId: Int, isComplete: Boolean)
}