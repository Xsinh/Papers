package com.implosion.papers.presentation.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.implosion.papers.R


@Composable
fun AnimatedExpandableFab(
    onVoiceClick: () -> Unit,
    onTextClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 45f else 0f,
        animationSpec = tween(300)
    )

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Кнопки появляются с задержкой
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(300, delayMillis = 0)) +
                    slideInVertically(initialOffsetY = { it / 3 }),
            exit = fadeOut(animationSpec = tween(300)) +
                    slideOutVertically(targetOffsetY = { it / 3 })
        ) {
            MiniFab(
                icon = Icons.Filled.Mic,
                label = stringResource(R.string.title_button_voice),
                onClick = onVoiceClick::invoke
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                    slideInVertically(initialOffsetY = { it / 3 }),
            exit = fadeOut(animationSpec = tween(300)) +
                    slideOutVertically(targetOffsetY = { it / 3 })
        ) {
            MiniFab(
                icon = Icons.Filled.Edit,
                label = stringResource(R.string.title_button_text),
                onClick = onTextClick::invoke
            )
        }

        // Главная кнопка с вращением
        FloatingActionButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.title_button_add),
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
fun MiniFab(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge
        )

        SmallFloatingActionButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        }
    }
}

@Preview
@Composable
private fun AnimatedExpandableFabPreview() {
    AnimatedExpandableFab(
        onTextClick = {},
        onVoiceClick = {}
    )
}