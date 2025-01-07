package com.implosion.papers.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.domain.model.NoteModel
import com.implosion.domain.repository.HashTagRepository
import com.implosion.domain.repository.NoteRepository
import com.implosion.domain.repository.SearchRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MainViewModel(
    private val noteRepository: NoteRepository,
    private val hashTagRepository: HashTagRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _noteList =
        MutableStateFlow<List<NoteModel>>(emptyList())
    val noteList: StateFlow<List<NoteModel>>
        get() = _noteList
            .asStateFlow()

    private var notesCache: List<NoteModel> by Delegates.observable(emptyList()) { _, oldValue, newValue ->
        viewModelScope.launch {
            val updatedNotes = newValue.map { note ->
                async {
                    val tags = hashTagRepository.getAllTagsFromNote(note.noteId)
                    note.copy(hashTagList = tags)
                }
            }.awaitAll()

            _noteList.emit(updatedNotes)
        }
    }

    init {
        viewModelScope.launch {
            refreshNotes()
        }
    }

    fun setHashTag(noteId: Int, hashTag: String) {
        viewModelScope.launch {
            hashTagRepository.addTagToNote(noteId = noteId, tagName = hashTag)

            refreshNotes()
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            val results =
                searchRepository.search(query)

            val updatedNotes = results.map { note ->
                async {
                    val tags = hashTagRepository.getAllTagsFromNote(note.noteId)
                    note.copy(hashTagList = tags)
                }
            }.awaitAll()

            _noteList.emit(updatedNotes)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            noteRepository.deleteNoteById(noteId)
            refreshNotes()
        }
    }

    private suspend fun refreshNotes() {
        notesCache = noteRepository.getAllNotes()
    }
}