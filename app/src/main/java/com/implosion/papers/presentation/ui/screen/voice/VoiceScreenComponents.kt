package com.implosion.papers.presentation.ui.screen.voice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Transcribe
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.implosion.domain.model.record.RecordingState
import com.implosion.papers.R
import com.implosion.papers.presentation.navigation.NavigationScreen
import com.implosion.papers.presentation.ui.theme.PapersTheme
import com.implosion.papers.presentation.ui.theme.Red70
import java.net.URLEncoder

//_____________________________________AudioWaveform________________________________________________

@Composable
fun AudioWaveform(
    audioData: List<Float>,
    isRecording: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val width = size.width
            val height = size.height
            val centerY = height / 2

            if (audioData.isEmpty()) {
                // Placeholder когда нет данных
                drawCircle(
                    color = Color.Gray.copy(alpha = 0.3f),
                    radius = 30f,
                    center = Offset(width / 2, centerY)
                )
            } else {
                val barWidth = 6.dp.toPx()
                val spacing = 2.dp.toPx()

                audioData.forEachIndexed { index, amplitude ->
                    // Позиция справа налево
                    val x = width - (audioData.size - index) * (barWidth + spacing)

                    // Высота с минимальным значением
                    val barHeight = maxOf(
                        amplitude * height * 0.6f,
                        8.dp.toPx() // Минимальная высота для видимости
                    )

                    val color = if (isRecording) {
                        Color(0xFF00E676).copy(alpha = 0.5f + amplitude * 0.5f)
                    } else {
                        Color(0xFF4A90E2).copy(alpha = 0.5f + amplitude * 0.5f)
                    }

                    drawRoundRect(
                        color = color,
                        topLeft = Offset(x, centerY - barHeight / 2),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AudioWaveformPreview() {
    PapersTheme {
        AudioWaveform(
            audioData = listOf(
                0.2f,
                0.5f,
                0.2f,
                1.0f,
                0.5f,
                0.2f,
                0.5f,
                0.2f,
                0.5f,
                0.2f,
                0.2f,
                0.2f,
                0.2f,
                0.5f,
                0.2f,
                0.8f,
            ),
            isRecording = true
        )
    }
}

//_____________________________________RecordingControls____________________________________________

@Composable
fun RecordingControls(
    recordingState: RecordingState,
    onRecordClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Кнопка остановки
        AnimatedVisibility(
            visible = recordingState != RecordingState.IDLE,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            FloatingActionButton(
                onClick = onStopClick,
                containerColor = Red70,
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Stop,
                    contentDescription = "Остановить",
                    tint = Color.White
                )
            }
        }

        // Главная кнопка записи
        FloatingActionButton(
            onClick = onRecordClick,
            containerColor = when (recordingState) {
                RecordingState.IDLE -> Color(0xFF00E676)
                RecordingState.RECORDING -> Color(0xFFFFA726)
                RecordingState.PAUSED -> Color(0xFF00E676)
                RecordingState.STOPPED -> Color(0xFF00E676)
            },
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                imageVector = when (recordingState) {
                    RecordingState.IDLE -> Icons.Filled.Mic
                    RecordingState.RECORDING -> Icons.Filled.Pause
                    RecordingState.PAUSED -> Icons.Filled.PlayArrow
                    RecordingState.STOPPED -> Icons.Filled.Mic
                },
                contentDescription = when (recordingState) {
                    RecordingState.IDLE -> "Начать запись"
                    RecordingState.RECORDING -> "Пауза"
                    RecordingState.PAUSED -> "Продолжить"
                    RecordingState.STOPPED -> "Новая запись"
                },
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun RecordingControlsPreview() {
    PapersTheme {
        RecordingControls(
            recordingState = RecordingState.RECORDING,
            onRecordClick = {},
            onStopClick = {}
        )
    }
}

//_____________________________________TranscriptionSection_________________________________________

@Composable
fun TranscriptionSection(
    isTranscribed: Boolean,
    isTranscribing: Boolean,
    transcriptionText: String,
    onTranscribeClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController? = null,
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.title_transcription),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            when {
                isTranscribing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                !isTranscribed -> {
                    val borderWidth = 1.dp
                    val borderColor = colorScheme.primary.copy(alpha = 0.1f)
                    OutlinedButton(
                        onClick = onTranscribeClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(64),
                        border = BorderStroke(borderWidth, borderColor),
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                modifier = Modifier.padding(end = 18.dp),
                                text = stringResource(R.string.title_to_transcription),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Icon(
                                imageVector = Icons.Filled.Transcribe,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }

                else -> {
                    navController?.navigate(
                        "${NavigationScreen.DetailsWriteNote.route}?text=${
                            URLEncoder.encode(
                                transcriptionText,
                                "UTF-8"
                            )
                        }"
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun TranscriptionSectionPreview() {
    PapersTheme {
        TranscriptionSection(
            isTranscribed = false,
            isTranscribing = false,
            transcriptionText = "Транскрибция аудио",
            onTranscribeClick = {}
        )
    }
}