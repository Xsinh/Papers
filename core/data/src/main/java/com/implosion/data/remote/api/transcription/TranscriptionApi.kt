package com.implosion.data.remote.api.transcription

import com.implosion.data.remote.model.transcription.TranscriptionResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TranscriptionApi {

    suspend fun postTranscription(audioFile: File): Flow<TranscriptionResponse>
}