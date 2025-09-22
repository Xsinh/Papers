package com.implosion.data.remote.api.summarization

import com.implosion.data.remote.model.GenerateRequest
import com.implosion.data.remote.model.GenerateResponse
import kotlinx.coroutines.flow.Flow

interface OllamaApi {


    suspend fun generate(request: GenerateRequest): Flow<GenerateResponse>
}