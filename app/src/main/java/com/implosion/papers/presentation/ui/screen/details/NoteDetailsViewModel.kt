package com.implosion.papers.presentation.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.papers.domain.model.NoteModel
import com.implosion.papers.domain.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _note = MutableStateFlow<NoteModel?>(null)
    val note
        get() = _note.asStateFlow()


    fun createNote(title: String? = null, content: String) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            noteRepository.createNote(
                NoteModel(
                    title = title,
                    content = content,
                    createdAt = currentTime,
                    updatedAt = currentTime,
                )
            )
        }
    }

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            _note.emit(noteRepository.getNote(noteId))
        }
    }
}