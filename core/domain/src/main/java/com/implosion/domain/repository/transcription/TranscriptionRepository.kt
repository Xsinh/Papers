package com.implosion.domain.repository.transcription

import com.implosion.domain.model.TranscriptionDomainModel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface TranscriptionRepository {

    suspend fun audioTranscription(audioFile: File): Flow<TranscriptionDomainModel>
}