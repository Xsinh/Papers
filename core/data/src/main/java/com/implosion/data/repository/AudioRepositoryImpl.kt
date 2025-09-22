package com.implosion.data.repository

import com.implosion.data.source.AudioRecorderDataSource
import com.implosion.domain.model.record.AudioRecording
import com.implosion.domain.repository.transcription.AudioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class AudioRepositoryImpl(
    private val audioRecorderDataSource: AudioRecorderDataSource
) : AudioRepository {
    private var isRecording = false

    override suspend fun startRecording(): Result<Unit> {

        return audioRecorderDataSource.startRecording().map {
            isRecording = true
        }
    }

    override suspend fun pauseRecording(): Result<Unit> {
        return audioRecorderDataSource.pauseRecording()
    }

    override suspend fun resumeRecording(): Result<Unit> {
        return audioRecorderDataSource.resumeRecording()
    }

    override suspend fun stopRecording(): Result<AudioRecording> {
        isRecording = false
        return audioRecorderDataSource.stopRecording().map { (filePath, duration) ->
            AudioRecording(
                id = UUID.randomUUID().toString(),
                filePath = filePath,
                duration = duration,
                timestamp = System.currentTimeMillis()
            )
        }
    }

    override fun getAmplitude(): Flow<Float> {
        return audioRecorderDataSource.amplitude  // Возвращаем Flow напрямую из DataSource
    }

    override suspend fun getAllRecordings(): Result<List<AudioRecording>> {
       //  Implement with local database
        return Result.success(emptyList())
    }

    override suspend fun deleteRecording(id: String): Result<Unit> {
        // Implement with local database
        return Result.success(Unit)
    }
}