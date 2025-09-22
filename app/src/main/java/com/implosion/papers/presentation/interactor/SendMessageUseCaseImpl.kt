package com.implosion.papers.presentation.interactor

import com.implosion.domain.model.ChatMessage
import com.implosion.domain.repository.summarization.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class SendMessageUseCaseImpl(
    private val repository: ChatRepository
) : SendMessageUseCase {

    override fun sendMessage(
        model: String,
        prompt: String
    ): Flow<ChatMessage> = flow {
        emitAll(repository.sendMessage(model, prompt))
    }
}