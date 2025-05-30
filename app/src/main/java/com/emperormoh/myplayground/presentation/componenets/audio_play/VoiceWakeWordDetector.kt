package com.emperormoh.myplayground.presentation.componenets.audio_play

import ai.picovoice.porcupine.Porcupine
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoiceWakeWordDetector(context: Context) {
    private val porcupine: Porcupine = Porcupine.Builder()
        .setAccessKey("WCAL7cMwkJq2X6GFhoYi7ALPSPlpueBIsRFJ8aBeKYPjilf4v2PXnA==")
        .setKeywordPath("Wake_Up_en_android_v3_0_0.ppn")
        .build(context)

    private val audioRecorder = AudioRecorder(context, porcupine.frameLength, porcupine.sampleRate)

    private var listeningJob: Job? = null

    fun startListening(onWakeWordDetected: () -> Unit) {
        listeningJob = CoroutineScope(Dispatchers.IO).launch {
            audioRecorder.start()
            val pcmBuffer = ShortArray(porcupine.frameLength)

            while (isActive) {
                audioRecorder.read(pcmBuffer)
                val keywordIndex = porcupine.process(pcmBuffer)
                if (keywordIndex >= 0) {
                    withContext(Dispatchers.Main) {
                        onWakeWordDetected()
                    }
                }
            }
        }
    }

    fun stopListening() {
        listeningJob?.cancel()
        audioRecorder.stop()
        porcupine.delete()
    }
}
