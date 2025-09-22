package com.implosion.papers.presentation.use_case

import com.implosion.domain.repository.transcription.AudioRepository

class ResumeRecordingUseCase (
    private val repository: AudioRepository
) {

    suspend operator fun invoke(): Result<Unit> {
        return repository.resumeRecording()
    }
}