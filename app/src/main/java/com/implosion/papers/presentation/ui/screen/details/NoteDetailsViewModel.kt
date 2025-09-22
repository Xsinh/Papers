package com.implosion.papers.presentation.ui.screen.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.domain.model.NoteModel
import com.implosion.domain.repository.papers.NoteRepository
import com.implosion.papers.presentation.interactor.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    private val noteRepository: NoteRepository,
    private val sendMessageUseCase: SendMessageUseCase,
) : ViewModel() {

    private val _note = MutableStateFlow<NoteModel?>(null)
    val note
        get() = _note.asStateFlow()

    fun magic() {
        val result = mutableListOf<String>()
        viewModelScope.launch {
            sendMessageUseCase.sendMessage(prompt = "Изучи текст, выдели основные положения и идеи и напиши их более иформативно и доступно, конечный размер должен быть не больше 30: ${_note.value?.content}")
                .catch { e ->
                    Log.d("###", e.toString())
                }
                .collect { data ->
                    result.add(data.text.removeSuffix(","))
                    _note.emit(_note.value?.copy(content = result.joinToString()))
                }
        }
    }

    fun createNote(title: String? = null, content: String) {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()

            noteRepository.createNote(
                NoteModel(
                    title = title,
                    content = content,
                    createdAt = currentTime,
                    updatedAt = currentTime,
                    isMarkedAsComplete = false
                )
            )
        }
    }

    fun getNote(noteId: Int) {
        viewModelScope.launch {
            _note.emit(noteRepository.getNote(noteId))
        }
    }

    fun editNote(content: String) {
        viewModelScope.launch {
            _note.value?.copy()?.let { note ->
                noteRepository.updateNote(
                    note.copy(content = content)
                )
            }
        }
    }
}