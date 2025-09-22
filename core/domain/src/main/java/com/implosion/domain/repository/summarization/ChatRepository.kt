package com.implosion.domain.repository.summarization

import com.implosion.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun sendMessage(model: String, prompt: String): Flow<ChatMessage>
}