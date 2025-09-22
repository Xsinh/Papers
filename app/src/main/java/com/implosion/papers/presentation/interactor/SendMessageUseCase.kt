package com.implosion.papers.presentation.interactor

import com.implosion.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface SendMessageUseCase {

    fun sendMessage(
        model: String = "phi3:3.8b-mini-4k-instruct-q4_K_M",
        prompt: String
    ): Flow<ChatMessage>
}