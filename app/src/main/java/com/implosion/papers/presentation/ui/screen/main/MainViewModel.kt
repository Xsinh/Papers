package com.implosion.papers.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.domain.model.NoteModel
import com.implosion.papers.presentation.interactor.NoteInteractor
import com.implosion.papers.presentation.provider.SharedPreferenceShakeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteInteractor: NoteInteractor,
    private val shakePreferenceService: SharedPreferenceShakeService
) : ViewModel() {

    private val _noteList = MutableStateFlow<List<NoteModel>>(emptyList())
    val noteList: StateFlow<List<NoteModel>> = _noteList.asStateFlow()

    private val _isShakeNoteList = MutableStateFlow<Boolean>(shakePreferenceService.isNoteListShake)
    val isShakeNoteList = _isShakeNoteList.asStateFlow()

    init {
        viewModelScope.launch {
            refreshNotes()
        }
    }

    fun setShakeNoteListState(isShake: Boolean){
        shakePreferenceService.setNoteListShake(isShake)

        viewModelScope.launch{
            _isShakeNoteList.emit(shakePreferenceService.isNoteListShake)
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

    fun markNoteItemAsComplete(noteId: Int, isComplete: Boolean) {
        viewModelScope.launch {
            noteInteractor.markNoteItemAsComplete(noteId = noteId, isComplete = isComplete)
            refreshNotes()
        }
    }
}