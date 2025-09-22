package com.implosion.domain.model.record

/**
 * Модель для записи с микрофона
 */
data class  AudioRecording(
    val id: String,
    val filePath: String,
    val duration: Long,
    val timestamp: Long,
    val transcription: String? = null
)