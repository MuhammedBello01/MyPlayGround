package com.emperormoh.myplayground.presentation.componenets.audio_play.vosk

import org.vosk.Model
import org.vosk.Recognizer
//import org.vosk.android.RecognitionListener
//import org.vosk.android.SpeechService
//import org.vosk.android.StorageService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
//import org.vosk.Recognizer
//import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

class VoskWakeWordDetector(
    private val context: Context,
    private val wakeWords: List<String>,
    private val sampleRate: Int = 16000
) {

    private var model: Model? = null
    private var recognizer: Recognizer? = null
    private var audioRecord: AudioRecord? = null
    private val isListening = AtomicBoolean(false)

    // For filtering false positives: simple approach with confidence checks and confirmation count
    private val wakeWordConfirmationsRequired = 3
    private var wakeWordDetectionCount = 0

    private var recognitionJob: Job? = null

    /**
     * Start recognition; returns flow of recognized text, including wake word detection event
     */
    fun startRecognition(
        onPartialResult: (String) -> Unit,
        onFinalResult: (String) -> Unit,
        onWakeWordDetected: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (isListening.get()) return

        isListening.set(true)
        wakeWordDetectionCount = 0

        recognitionJob = CoroutineScope(Dispatchers.IO).launch {
            try {
//                if (model == null) {
//                    model = Model(context.assets, "model")
//                }
                val modelDir = copyModelFromAssets(context, "vosk_model_small_en_us_0_15")

                // Initialize the model
                model = Model(modelDir.absolutePath)
                recognizer = Recognizer(model, sampleRate.toFloat())
                val minBufferSize = AudioRecord.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED
                ){
                    audioRecord = AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        sampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        minBufferSize
                    )
                } else {
                    throw SecurityException("RECORD_AUDIO permission not granted")
                }


                val buffer = ByteArray(minBufferSize)

                audioRecord?.startRecording()

                while (isListening.get()) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        val isFinal = recognizer?.acceptWaveForm(buffer, read) ?: false

                        if (isFinal) {
                            val resultJson = recognizer?.result ?: ""
                            val text = extractTextFromJson(resultJson)
                            onFinalResult(text)
                            if (checkWakeWordDetected(text)) {
                                wakeWordDetectionCount++
                                if (wakeWordDetectionCount >= wakeWordConfirmationsRequired) {
                                    onWakeWordDetected()
                                    stopRecognition()
                                    break
                                }
                            } else {
                                wakeWordDetectionCount = 0
                            }
                        } else {
                            val partialJson = recognizer?.partialResult ?: ""
                            val partialText = extractTextFromJson(partialJson)
                            onPartialResult(partialText)
                        }
                    }
                }

                audioRecord?.stop()
                audioRecord?.release()
                recognizer?.close()
                model?.close()
            } catch (e: IOException) {
                onError(e)
            }
        }
    }

    fun stopRecognition() {
        isListening.set(false)
        recognitionJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        recognizer?.close()
        model?.close()
    }

    private fun checkWakeWordDetected(text: String): Boolean {
        return wakeWords.any { wakeWord ->
            text.contains(wakeWord, ignoreCase = true)
        }
    }

    private fun extractTextFromJson(json: String): String {
        return try {
            val regex = """"text"\s*:\s*"([^"]*)"""".toRegex()
            val match = regex.find(json)
            match?.groups?.get(1)?.value ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun copyModelFromAssets(context: Context, modelName: String): File {
        val modelDir = File(context.filesDir, modelName)
        if (!modelDir.exists()) {
            modelDir.mkdir()
            val assets = context.assets.list(modelName)
            assets?.forEach { asset ->
                context.assets.open("$modelName/$asset").use { input ->
                    FileOutputStream(File(modelDir, asset)).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
        return modelDir
    }
}
