package com.implosion.papers.presentation.use_case

import com.implosion.domain.repository.transcription.AudioRepository

class GetAmplitudeUseCase(
    private val repository: AudioRepository
) {
    operator fun invoke() = repository.getAmplitude()
}