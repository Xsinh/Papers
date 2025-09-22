package com.implosion.domain.repository.transcription

import com.implosion.domain.model.record.AudioRecording
import kotlinx.coroutines.flow.Flow

interface AudioRepository {

    suspend fun startRecording(): Result<Unit>

    suspend fun pauseRecording(): Result<Unit>

    suspend fun resumeRecording(): Result<Unit>

    suspend fun stopRecording(): Result<AudioRecording>

    fun getAmplitude(): Flow<Float>

    suspend fun getAllRecordings(): Result<List<AudioRecording>>

    suspend fun deleteRecording(id: String): Result<Unit>
}