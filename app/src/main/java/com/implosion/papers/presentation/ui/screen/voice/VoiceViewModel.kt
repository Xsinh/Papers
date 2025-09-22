package com.implosion.papers.presentation.ui.screen.voice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implosion.domain.model.record.RecordingState
import com.implosion.domain.repository.transcription.TranscriptionRepository
import com.implosion.papers.presentation.use_case.GetAmplitudeUseCase
import com.implosion.papers.presentation.use_case.PauseRecordingUseCase
import com.implosion.papers.presentation.use_case.ResumeRecordingUseCase
import com.implosion.papers.presentation.use_case.StartRecordingUseCase
import com.implosion.papers.presentation.use_case.StopRecordingUseCase
import com.implosion.papers.presentation.ui.screen.voice.model.VoiceUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class VoiceViewModel(
    private val transcriptionRepository: TranscriptionRepository,
    private val startRecordingUseCase: StartRecordingUseCase,
    private val pauseRecordingUseCase: PauseRecordingUseCase,
    private val resumeRecordingUseCase: ResumeRecordingUseCase,
    private val stopRecordingUseCase: StopRecordingUseCase,
    private val getAmplitudeUseCase: GetAmplitudeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private val _audioWaveData = MutableStateFlow<List<Float>>(emptyList())
    val audioWaveData: StateFlow<List<Float>> = _audioWaveData.asStateFlow()

    private val _permissionNeeded = MutableStateFlow(false)
    val permissionNeeded: StateFlow<Boolean> = _permissionNeeded.asStateFlow()

    init {
        observeAmplitude()
    }

    private fun observeAmplitude() {
        viewModelScope.launch {
            getAmplitudeUseCase().collect { amplitude ->
                val currentData = _audioWaveData.value.toMutableList()
                currentData.add(amplitude)
                if (currentData.size > 100) {
                    currentData.removeAt(0)
                }
                _audioWaveData.value = currentData
            }
        }
    }

    fun onRecordClick(context: Context) {
        viewModelScope.launch {
            when (_uiState.value.recordingState) {
                RecordingState.IDLE -> startRecording(context)
                RecordingState.RECORDING -> pauseRecording()
                RecordingState.PAUSED -> resumeRecording()
                RecordingState.STOPPED -> startRecording(context)
            }
        }
    }

    fun onStopClick() {
        viewModelScope.launch {
            stopRecording()
        }
    }

    private suspend fun startRecording(context: Context) {
        if (!hasAudioPermission(context)) {
            _permissionNeeded.value = true
            return
        }
        startRecordingUseCase().onSuccess {
            _uiState.update { it.copy(recordingState = RecordingState.RECORDING) }
            _audioWaveData.value = emptyList()
        }.onFailure { error ->
            _uiState.update {
                it.copy(error = error.message ?: "Ошибка начала записи")
            }
        }
    }

    private suspend fun pauseRecording() {
        pauseRecordingUseCase().onSuccess {
            _uiState.update { it.copy(recordingState = RecordingState.PAUSED) }
        }.onFailure { error ->
            _uiState.update {
                it.copy(error = error.message ?: "Ошибка паузы записи")
            }
        }
    }

    private suspend fun resumeRecording() {
        resumeRecordingUseCase().onSuccess {
            _uiState.update { it.copy(recordingState = RecordingState.RECORDING) }
        }.onFailure { error ->
            _uiState.update {
                it.copy(error = error.message ?: "Ошибка возобновления записи")
            }
        }
    }

    private suspend fun stopRecording() {
        stopRecordingUseCase().onSuccess { recording ->
            _uiState.update {
                it.copy(
                    recordingState = RecordingState.STOPPED,
                    currentRecording = recording
                )
            }
        }.onFailure { error ->
            _uiState.update {
                it.copy(error = error.message ?: "Ошибка остановки записи")
            }
        }
    }

    fun onTranscribeClick() {
        val file = File(_uiState.value.currentRecording?.filePath)

        if (!file.exists()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isTranscribing = true) }
            transcriptionRepository.audioTranscription(file)
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isTranscribing = false,
                            error = "Ошибка транскрибации: ${error.message}"
                        )
                    }
                }
                .collect { result ->
                    _uiState.update {
                        it.copy(
                            isTranscribed = true,
                            isTranscribing = false,
                            transcriptionText = result.text
                        )
                    }
                }

        }
    }

    fun onPermissionResult(granted: Boolean, context: Context) {

        viewModelScope.launch {
            _permissionNeeded.value = false
            if (granted) {
                startRecording(context)
            }
        }
    }

    private fun hasAudioPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}