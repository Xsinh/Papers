package com.implosion.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateResponse(
    val model: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    val response: String? = null,
    val done: Boolean? = null,
    @SerialName("done_reason")
    val doneReason: String? = null,
    val context: List<Int>? = null,
    @SerialName("total_duration")
    val totalDuration: Long? = null,
    @SerialName("load_duration")
    val loadDuration: Long? = null,
    @SerialName("prompt_eval_count")
    val promptEvalCount: Int? = null,
    @SerialName("prompt_eval_duration")
    val promptEvalDuration: Long? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    @SerialName("eval_duration")
    val evalDuration: Long? = null
)
