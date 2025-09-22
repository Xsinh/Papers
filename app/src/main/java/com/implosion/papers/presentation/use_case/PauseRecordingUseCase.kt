package com.implosion.papers.presentation.use_case

import com.implosion.domain.repository.transcription.AudioRepository

class PauseRecordingUseCase constructor(
    private val repository: AudioRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return repository.pauseRecording()
    }
}