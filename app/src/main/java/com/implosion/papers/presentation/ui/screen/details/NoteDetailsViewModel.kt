package com.implosion.papers.presentation.ui.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.papers.domain.model.NoteModel
import com.implosion.papers.domain.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    private val noteRepository: NoteRepository,
) : ViewModel() {

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
}