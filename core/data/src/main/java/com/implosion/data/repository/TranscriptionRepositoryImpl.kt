package com.implosion.data.repository

import com.implosion.data.database.entity.tag.mapper.toDomain
import com.implosion.data.remote.api.transcription.TranscriptionApi
import com.implosion.data.remote.model.transcription.TranscriptionResponse
import com.implosion.domain.model.TranscriptionDomainModel
import com.implosion.domain.repository.transcription.TranscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class TranscriptionRepositoryImpl(
    private val transcriptionApi: TranscriptionApi
) : TranscriptionRepository {

    override suspend fun audioTranscription(audioFile: File): Flow<TranscriptionDomainModel> {
        return transcriptionApi.postTranscription(audioFile).map(TranscriptionResponse::toDomain)
    }
}