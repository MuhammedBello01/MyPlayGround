package com.emperormoh.myplayground.presentation.componenets.audio_play

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.content.ContextCompat

class AudioRecorder(
    private val context: Context,
    private val frameLength: Int,
    private val sampleRate: Int
) {
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private var audioRecord: AudioRecord? = null

    fun start() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
        ) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            ).apply {
                startRecording()
            }
        } else {
            throw SecurityException("RECORD_AUDIO permission not granted")
        }
    }

    fun read(buffer: ShortArray): Int {
        return audioRecord?.read(buffer, 0, frameLength) ?: 0
    }

    fun stop() {
        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null
    }
}

