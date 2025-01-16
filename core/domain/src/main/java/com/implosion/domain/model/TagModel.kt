package com.implosion.domain.model

import androidx.compose.runtime.Stable

@Stable
data class TagModel(
    val tagId: Int,
    val name: String,
)
