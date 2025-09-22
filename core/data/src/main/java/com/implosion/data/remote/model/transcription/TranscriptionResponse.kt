package com.implosion.data.remote.model.transcription

import kotlinx.serialization.Serializable

@Serializable
data class TranscriptionResponse(
    val text: String
)
