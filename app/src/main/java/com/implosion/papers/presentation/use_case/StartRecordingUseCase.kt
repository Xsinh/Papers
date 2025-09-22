package com.implosion.papers.presentation.use_case

import com.implosion.domain.repository.transcription.AudioRepository

class StartRecordingUseCase(
    private val repository: AudioRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return repository.startRecording()
    }
}