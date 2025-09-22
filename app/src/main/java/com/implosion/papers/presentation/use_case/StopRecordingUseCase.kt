package com.implosion.papers.presentation.use_case

import com.implosion.domain.model.record.AudioRecording
import com.implosion.domain.repository.transcription.AudioRepository

class StopRecordingUseCase(
    private val repository: AudioRepository
) {
    suspend operator fun invoke(): Result<AudioRecording> {
        return repository.stopRecording()
    }
}