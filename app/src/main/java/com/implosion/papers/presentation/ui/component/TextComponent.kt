package com.implosion.papers.presentation.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationText(
    text: String,
    oldText: String,
) {
    Row {
        for (i in text.indices) {
            val oldChar = oldText.getOrNull(i)
            val newChar = text[i]
            val char = if (oldChar == newChar) {
                oldText[i]
            } else {
                text[i]
            }

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(durationMillis = 600)) { it } +
                            fadeIn(animationSpec = tween(durationMillis = 600))) togetherWith
                            (slideOutVertically(animationSpec = tween(durationMillis = 600)) { -it } +
                                    fadeOut(animationSpec = tween(durationMillis = 600)))
                }, label = "animationChar"
            ) { chars ->
                Text(
                    chars.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
    }
}