package com.implosion.papers.presentation.ui.screen.voice.model

import com.implosion.domain.model.record.AudioRecording
import com.implosion.domain.model.record.RecordingState

data class VoiceUiState(
    val recordingState: RecordingState = RecordingState.IDLE,
    val currentRecording: AudioRecording? = null,
    val isTranscribed: Boolean = false,
    val isTranscribing: Boolean = false,
    val transcriptionText: String = "",
    val error: String? = null
)