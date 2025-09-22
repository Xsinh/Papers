package com.implosion.data.remote.api.summarization

import com.implosion.data.remote.model.GenerateRequest
import com.implosion.data.remote.model.GenerateResponse
import com.implosion.network.NetworkClient
import io.ktor.client.call.body
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class OllamaApiImpl(
    private val client: NetworkClient
) : OllamaApi {

    override suspend fun generate(request: GenerateRequest): Flow<GenerateResponse> = flow {
        return@flow client.httpClient.preparePost("/api/generate") {
            setBody(request)
        }.execute { httpResponse ->
            val channel: ByteReadChannel = httpResponse.body()

            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line() ?: continue
                if (line.isNotBlank()) {
                    try {
                        val response = Json.decodeFromString<GenerateResponse>(line)
                        emit(response)

                        if (response.done == true) break
                    } catch (e: Exception) {
                        // Логируем или игнорируем невалидные строки
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}