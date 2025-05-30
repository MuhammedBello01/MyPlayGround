package com.emperormoh.myplayground.presentation.componenets.audio_play


import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Base64
import android.util.Log
import androidx.media.AudioAttributesCompat
import androidx.media.AudioFocusRequestCompat
import java.io.ByteArrayInputStream
import androidx.media.AudioManagerCompat
import java.io.InputStream

class PCM16AudioPlayer(
    private val context: Context,
    private val sampleRate: Int = 44100,
    private val channelConfig: Int = AudioFormat.CHANNEL_OUT_MONO
) {
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var isMuted = false
    private var currentVolume = 1.0f // Track volume ourselves
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    companion object {
        private const val TAG = "PCM16AudioPlayer"
    }
    private var playbackCompletionListener: (() -> Unit)? = null
    fun setPlaybackCompletionListener(listener: () -> Unit) {
        playbackCompletionListener = listener
    }

    private val audioFocusChangeListener = AudioManager.OnAudioFocusChangeListener {
        // Optionally handle audio focus changes here
    }

    private val audioFocusRequest by lazy {
        AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributesCompat.Builder()
                    .setUsage(AudioAttributesCompat.USAGE_MEDIA)
                    .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()
    }


    fun playPCM16FromString(encodedString: String, encoding: String = "base64") {
        Log.d(TAG, "Starting playback with encoding: $encoding")
        stop()

        val pcmData = when (encoding.lowercase()) {
            "base64" -> {
                val cleanBase64 = encodedString
                    .removePrefix("data:audio/pcm;base64,")
                    .removePrefix("data:audio/wav;base64,")
                    .replace("\\s".toRegex(), "") // ADDED: Remove any whitespace
                Base64.decode(cleanBase64, Base64.DEFAULT)
            }
            "hex" -> hexStringToByteArray(encodedString)
            else -> throw IllegalArgumentException("Unsupported encoding: $encoding")
        }

        Log.d(TAG, "Decoded PCM data size: ${pcmData.size} bytes")
        if (pcmData.isEmpty()) {
            Log.e(TAG, "PCM data is empty after decoding")
            return
        }
        playPCM16Stream(ByteArrayInputStream(pcmData))
    }

    private fun playPCM16Stream(inputStream: InputStream) {
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            channelConfig,
            AudioFormat.ENCODING_PCM_16BIT
        )
        Log.d(TAG, "Buffer size: $bufferSize")

        if (bufferSize == AudioTrack.ERROR_BAD_VALUE || bufferSize == AudioTrack.ERROR) {
            Log.e(TAG, "Invalid buffer size: $bufferSize")
            return
        }

        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(channelConfig)
                .build(),
            bufferSize,
            AudioTrack.MODE_STREAM,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        ).apply {
            setVolume(if (isMuted) 0f else currentVolume)
        }

        if (audioTrack?.state != AudioTrack.STATE_INITIALIZED) {
            Log.e(TAG, "AudioTrack failed to initialize. State: ${audioTrack?.state}")
            return
        }

        requestAudioFocus()
        audioTrack?.play()
        isPlaying = true

        Log.d(TAG, "AudioTrack started. Play state: ${audioTrack?.playState}")


        Thread {
            val buffer = ByteArray(bufferSize)
            var bytesRead: Int = 0

            var totalBytesWritten = 0

            try {
//                while (isPlaying && inputStream.read(buffer).also { bytesRead = it } != -1) {
//                    audioTrack?.write(buffer, 0, bytesRead)
//                }
                while (isPlaying && inputStream.read(buffer).also { bytesRead = it } != -1) {
                    if (bytesRead > 0) {
                        val bytesWritten = audioTrack?.write(buffer, 0, bytesRead) ?: 0
                        // ADDED: Track successful writes and log errors
                        if (bytesWritten > 0) {
                            totalBytesWritten += bytesWritten
                        } else {
                            Log.w(TAG, "AudioTrack write returned: $bytesWritten")
                        }
                    }
                }

                Log.d(TAG, "Playback finished. Total bytes written: $totalBytesWritten")

                // Only notify completion if playback wasn't manually stopped
                if (isPlaying) {
                    playbackCompletionListener?.invoke()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream.close()
                stop()
            }
        }.start()
    }

    fun mute() {
        if (!isMuted) {
            audioTrack?.setVolume(0f)
            isMuted = true
            Log.d(TAG, "Audio muted")
        }
    }

    fun unmute() {
        if (isMuted) {
            audioTrack?.setVolume(currentVolume)
            isMuted = false
            Log.d(TAG, "Audio unmuted")
        }

    }

    fun toggleMute() {
        if (isMuted) unmute() else mute()
    }

    fun setVolume(volume: Float) {
        require(volume in 0f..1f) { "Volume must be between 0 and 1" }
        currentVolume = volume
        if (!isMuted) {
            audioTrack?.setVolume(volume)
        }
        isMuted = volume == 0f
    }

    fun isMuted(): Boolean = isMuted

    fun getCurrentVolume(): Float = if (isMuted) 0f else currentVolume

    private fun hexStringToByteArray(hexString: String): ByteArray {
        val cleanedString = hexString.replace(" ", "").replace("\n", "")
        val len = cleanedString.length
        require(len % 2 == 0) { "Hex string must have even number of characters" }

        val data = ByteArray(len / 2)
        for (i in 0 until len step 2) {
            val byteStr = cleanedString.substring(i, i + 2)
            data[i / 2] = byteStr.toInt(16).toByte()
        }
        return data
    }

    fun stop() {
        isPlaying = false
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
        abandonAudioFocus()
    }

    private fun requestAudioFocus() {
//        val audioAttributes = AudioAttributesCompat.Builder()
//            .setUsage(AudioAttributesCompat.USAGE_MEDIA)
//            .setContentType(AudioAttributesCompat.CONTENT_TYPE_MUSIC)
//            .build()
//
//        val focusRequest = AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN)
//            .setAudioAttributes(audioAttributes)
//            .setOnAudioFocusChangeListener { }
//            .build()
//
//        AudioManagerCompat.requestAudioFocus(audioManager, focusRequest)
            AudioManagerCompat.requestAudioFocus(audioManager, audioFocusRequest)
    }

    private fun abandonAudioFocus() {
//        AudioManagerCompat.abandonAudioFocusRequest(
//            audioManager,
//            AudioFocusRequestCompat.Builder(AudioManagerCompat.AUDIOFOCUS_GAIN).build()
//        )
        AudioManagerCompat.abandonAudioFocusRequest(audioManager, audioFocusRequest)
    }
}