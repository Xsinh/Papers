package com.implosion.data.remote.api.transcription

import com.implosion.data.remote.model.transcription.TranscriptionResponse
import com.implosion.network.NetworkClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class TranscriptionApiImpl(
    private val client: NetworkClient
) : TranscriptionApi {

    override suspend fun postTranscription(audioFile: File): Flow<TranscriptionResponse> = flow {
        val response = client.httpClient.post("transcribe") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("file", audioFile.readBytes(), Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=\"${audioFile.name}\"")
                        })
                    }
                )
            )
        }

        emit(response.body())
    }
}