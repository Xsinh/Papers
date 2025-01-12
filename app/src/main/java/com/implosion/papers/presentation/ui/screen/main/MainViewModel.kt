package com.implosion.papers.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.domain.model.NoteModel
import com.implosion.papers.presentation.interactor.NoteInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteInteractor: NoteInteractor
) : ViewModel() {

    private val _noteList = MutableStateFlow<List<NoteModel>>(emptyList())
    val noteList: StateFlow<List<NoteModel>> = _noteList.asStateFlow()

    init {
        viewModelScope.launch {
            refreshNotes()
        }
    }

    fun setHashTag(noteId: Int, hashTag: String) {
        viewModelScope.launch {
            noteInteractor.addHashTag(noteId, hashTag)
            refreshNotes()
        }
    }

    fun deleteHashTag(hashTagId: Int, noteId: Int) {
        viewModelScope.launch {
            noteInteractor.deleteHashTag(hashTagId, noteId)
            refreshNotes()
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            val results = noteInteractor.searchNotes(query)
            _noteList.emit(results)
        }
    }

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            noteInteractor.deleteNote(noteId)
            refreshNotes()
        }
    }

    private suspend fun refreshNotes() {
        val updatedNotes = noteInteractor.getNotesWithTags()
        _noteList.emit(updatedNotes)
    }
}