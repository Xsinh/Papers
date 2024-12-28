package com.implosion.papers.presentation.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.papers.domain.model.NoteModel
import com.implosion.papers.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _noteList = MutableStateFlow<List<NoteModel>>(emptyList())
    val noteList: StateFlow<List<NoteModel>>
        get() = _noteList
            .asStateFlow()

    fun loadNotes() {
        viewModelScope.launch {
            _noteList.emit(noteRepository.getAllNotes())
        }
    }
}