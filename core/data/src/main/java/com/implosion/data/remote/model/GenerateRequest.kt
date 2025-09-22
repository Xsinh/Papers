package com.implosion.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = true,
    val options: GenerateOptions? = null
)

@Serializable
data class GenerateOptions(
    val temperature: Double? = null,
    val top_k: Int? = null,
    val top_p: Double? = null,
    val num_predict: Int? = null,
    val stop: List<String>? = null
)