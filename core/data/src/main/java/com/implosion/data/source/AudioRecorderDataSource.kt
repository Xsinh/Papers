package com.implosion.data.source

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioRecorderDataSource(
    private val context: Context
) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentFilePath: String? = null
    private var recordingStartTime: Long = 0
    private var pausedDuration: Long = 0
    private var lastPauseTime: Long = 0
    private var isRecordingActive = false
    private var isPaused = false

    private val _amplitude = MutableStateFlow(0f)
    val amplitude: Flow<Float> = _amplitude

    // ✅ Добавляем CoroutineScope для amplitude
    private val amplitudeScope = CoroutineScope(Dispatchers.Default)
    private var amplitudeJob: Job? = null

    suspend fun startRecording(): Result<String> {
        return try {
            cleanup()

            val fileName = generateFileName()
            val file = File(context.getExternalFilesDir(null), "recordings")
            if (!file.exists()) {
                file.mkdirs()
            }
            currentFilePath = File(file, fileName).absolutePath
            Log.d("AudioRecorder", "File saved to: $currentFilePath")

            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(currentFilePath)

                prepare()
                start()
            }

            recordingStartTime = System.currentTimeMillis()
            pausedDuration = 0
            isRecordingActive = true
            isPaused = false

            // ✅ ВАЖНО: Запускаем мониторинг amplitude
            startAmplitudeMonitoring()

            Log.d("AudioRecorder", "Recording started: $currentFilePath")
            Result.success(currentFilePath!!)
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error starting recording", e)
            cleanup()
            Result.failure(e)
        }
    }

    // ✅ Новый метод для мониторинга amplitude
    private fun startAmplitudeMonitoring() {
        amplitudeJob?.cancel()
        amplitudeJob = amplitudeScope.launch {
            while (isActive && isRecordingActive) {
                try {
                    if (!isPaused) {
                        val amplitude = mediaRecorder?.maxAmplitude ?: 0
                        val normalized = if (amplitude > 0) {
                            (amplitude.toFloat() / 32767f).coerceIn(0f, 1f)
                        } else {
                            0.05f // Минимальное значение для видимости
                        }
                        _amplitude.emit(normalized)
                        Log.d("AudioRecorder", "Amplitude: $amplitude -> normalized: $normalized")
                    }
                } catch (e: Exception) {
                    Log.e("AudioRecorder", "Error reading amplitude", e)
                }
                delay(50) // Обновление каждые 50мс
            }
        }
    }

    fun pauseRecording(): Result<Unit> {
        return try {
            if (!isRecordingActive || isPaused) {
                return Result.failure(IllegalStateException("Not recording or already paused"))
            }

            mediaRecorder?.pause()
            lastPauseTime = System.currentTimeMillis()
            isPaused = true
            amplitudeJob?.cancel() // ✅ Останавливаем мониторинг
            Log.d("AudioRecorder", "Recording paused")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error pausing recording", e)
            Result.failure(e)
        }
    }

    fun resumeRecording(): Result<Unit> {
        return try {
            if (!isRecordingActive || !isPaused) {
                return Result.failure(IllegalStateException("Not paused"))
            }

            mediaRecorder?.resume()
            pausedDuration += System.currentTimeMillis() - lastPauseTime
            isPaused = false
            startAmplitudeMonitoring() // ✅ Возобновляем мониторинг
            Log.d("AudioRecorder", "Recording resumed")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error resuming recording", e)
            Result.failure(e)
        }
    }

    suspend fun stopRecording(): Result<Pair<String, Long>> {
        return try {
            if (!isRecordingActive) {
                Log.w("AudioRecorder", "Trying to stop but not recording")
                return Result.failure(IllegalStateException("Not recording"))
            }

            val duration = System.currentTimeMillis() - recordingStartTime - pausedDuration
            val filePath = currentFilePath ?: return Result.failure(IllegalStateException("No file path"))

            // ✅ Останавливаем мониторинг amplitude
            amplitudeJob?.cancel()

            try {
                mediaRecorder?.apply {
                    stop()
                    reset()
                    release()
                }

                // ✅ ЛОГИ ДЛЯ ПРОВЕРКИ ФАЙЛА
                val file = File(filePath)
                Log.d("AudioRecorder", "=== RECORDING STOPPED ===")
                Log.d("AudioRecorder", "File path: $filePath")
                Log.d("AudioRecorder", "File exists: ${file.exists()}")
                Log.d("AudioRecorder", "File size: ${file.length()} bytes")
                Log.d("AudioRecorder", "Absolute path: ${file.absolutePath}")
                Log.d("AudioRecorder", "Duration: $duration ms")
                Log.d("AudioRecorder", "========================")

            } catch (e: Exception) {
                Log.e("AudioRecorder", "Error stopping MediaRecorder", e)
            } finally {
                mediaRecorder = null
                currentFilePath = null
                isRecordingActive = false
                isPaused = false
                _amplitude.value = 0f
            }

            Result.success(Pair(filePath, duration))
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error in stopRecording", e)
            cleanup()
            Result.failure(e)
        }
    }

    // ✅ Убираем метод updateAmplitude - он больше не нужен
    // Amplitude теперь обновляется автоматически в startAmplitudeMonitoring()

    private suspend fun cleanup() {
        amplitudeJob?.cancel() // ✅ Отменяем мониторинг
        try {
            mediaRecorder?.apply {
                if (isRecordingActive) {
                    try {
                        stop()
                    } catch (e: Exception) {
                        Log.e("AudioRecorder", "Error stopping in cleanup", e)
                    }
                }
                reset()
                release()
            }
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Error in cleanup", e)
        } finally {
            mediaRecorder = null
            currentFilePath = null
            isRecordingActive = false
            isPaused = false
            _amplitude.emit(0f)
        }
    }

    private fun generateFileName(): String {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            .format(Date())
        return "recording_$timestamp.m4a"
    }

    suspend fun release() {
        amplitudeJob?.cancel() // ✅ Отменяем мониторинг
        cleanup()
    }
}