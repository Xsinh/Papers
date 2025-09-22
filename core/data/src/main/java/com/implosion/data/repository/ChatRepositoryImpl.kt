package com.implosion.data.repository

import com.implosion.data.database.entity.note.mapper.toDomain
import com.implosion.data.remote.api.summarization.OllamaApi
import com.implosion.data.remote.model.GenerateRequest
import com.implosion.domain.model.ChatMessage
import com.implosion.domain.repository.summarization.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepositoryImpl(
    private val apiService: OllamaApi
) : ChatRepository {

    override suspend fun sendMessage(
        model: String,
        prompt: String
    ): Flow<ChatMessage> {
        val request = GenerateRequest(
            model = model,
            prompt = prompt,
        )
        return apiService.generate(request).map { it.toDomain() }
    }
}