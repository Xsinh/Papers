package com.implosion.papers.presentation.ui.screen.voice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.implosion.domain.model.record.RecordingState
import com.implosion.papers.R
import com.implosion.papers.presentation.ui.theme.PapersTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VoiceScreen(
    modifier: Modifier = Modifier,
    navController: NavController? = null
) {
    val viewModel: VoiceViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val audioWaveData by viewModel.audioWaveData.collectAsState()

    val context = LocalContext.current

    val audioPermissionState = rememberPermissionState(
        android.Manifest.permission.RECORD_AUDIO
    ) { granted ->
        viewModel.onPermissionResult(granted, context)
    }

    val permissionNeeded by viewModel.permissionNeeded.collectAsState()

    LaunchedEffect(permissionNeeded) {
        if (permissionNeeded) {
            audioPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            // Показать Snackbar или Toast
            viewModel.clearError()
        }
    }

    PapersTheme {
        Scaffold(content = { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
            ) {
                VoiceRecordingScreen(
                    recordingState = uiState.recordingState,
                    audioWaveData = audioWaveData,
                    isTranscribed = uiState.isTranscribed,
                    isTranscribing = uiState.isTranscribing,
                    transcriptionText = uiState.transcriptionText,
                    onRecordClick = { viewModel.onRecordClick(context) },
                    onStopClick = viewModel::onStopClick,
                    onTranscribeClick = viewModel::onTranscribeClick,
                    navController = navController,
                )
            }
        })
    }
}

@Composable
fun VoiceRecordingScreen(
    recordingState: RecordingState,
    audioWaveData: List<Float>,
    isTranscribed: Boolean,
    isTranscribing: Boolean,
    transcriptionText: String,
    onRecordClick: () -> Unit,
    onStopClick: () -> Unit,
    onTranscribeClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.title_voice_record),
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(18.dp)
        )

        if (recordingState == RecordingState.IDLE) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.title_button_click_to_record),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            AudioWaveform(
                audioData = audioWaveData,
                isRecording = recordingState == RecordingState.RECORDING,
                modifier = Modifier
                    .padding(16.dp)
            )
        }

        RecordingControls(
            recordingState = recordingState,
            onRecordClick = onRecordClick,
            onStopClick = onStopClick,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        AnimatedVisibility(
            visible = recordingState == RecordingState.STOPPED,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {

            TranscriptionSection(
                isTranscribed = isTranscribed,
                isTranscribing = isTranscribing,
                transcriptionText = transcriptionText,
                onTranscribeClick = onTranscribeClick,
                navController = navController
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun VoiceScreenPreview() {
    PapersTheme {
        VoiceRecordingScreen(
            recordingState = RecordingState.IDLE,
            audioWaveData = listOf(),
            isTranscribed = false,
            isTranscribing = false,
            transcriptionText = "",
            onStopClick = {},
            onRecordClick = {},
            onTranscribeClick = {}
        )
    }
}