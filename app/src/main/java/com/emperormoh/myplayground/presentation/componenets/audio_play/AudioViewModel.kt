package com.emperormoh.myplayground.presentation.componenets.audio_play

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

sealed class AudioState {
    object Idle : AudioState()
    object Loading : AudioState()
    object Playing : AudioState()
    data class Error(val message: String) : AudioState()
}

class AudioViewModel(context: Context) : ViewModel() {
    private val audioPlayer = PCM16AudioPlayer(context)
    private val client = OkHttpClient()

    // UI states
    private val _audioState = MutableStateFlow<AudioState>(AudioState.Idle)
    val audioState: StateFlow<AudioState> = _audioState.asStateFlow()

    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val _volumeLevel = MutableStateFlow(1f)
    val volumeLevel: StateFlow<Float> = _volumeLevel.asStateFlow()

    // Playback completion state
    private val _playbackComplete = MutableStateFlow(false)
    val playbackComplete: StateFlow<Boolean> = _playbackComplete.asStateFlow()

    init {
        // Setup playback completion listener
        audioPlayer.setPlaybackCompletionListener {
            viewModelScope.launch {
                _playbackComplete.value = true
                _audioState.value = AudioState.Idle
            }
        }
    }

    private fun startPlayback() {
        // Reset states when starting new playback
        _playbackComplete.value = false
        _audioState.value = AudioState.Loading
    }

    fun fetchAndPlayAudio(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                startPlayback()

                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        _audioState.value = AudioState.Error("Network error: ${response.code}")
                        return@use
                    }

                    response.body?.string()?.let { responseString ->
                        val encoding = if (responseString.matches(Regex("^[0-9a-fA-F ]+$"))) {
                            "hex"
                        } else {
                            "base64"
                        }

                        audioPlayer.playPCM16FromString(responseString, encoding)
                        _audioState.value = AudioState.Playing
                        _isMuted.value = audioPlayer.isMuted()
                        _volumeLevel.value = audioPlayer.getCurrentVolume()
                    }
                }
            } catch (e: Exception) {
                _audioState.value = AudioState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun playLocalString(encodedString: String, encoding: String = "base64") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                startPlayback()
                audioPlayer.playPCM16FromString(encodedString, encoding)
                _audioState.value = AudioState.Playing
                _isMuted.value = audioPlayer.isMuted()
                _volumeLevel.value = audioPlayer.getCurrentVolume()
            } catch (e: Exception) {
                _audioState.value = AudioState.Error(e.message ?: "Playback error")
            }
        }
    }

    fun toggleMute() {
        audioPlayer.toggleMute()
        _isMuted.value = audioPlayer.isMuted()
        _volumeLevel.value = audioPlayer.getCurrentVolume()
    }

    fun setVolume(volume: Float) {
        audioPlayer.setVolume(volume)
        _isMuted.value = audioPlayer.isMuted()
        _volumeLevel.value = audioPlayer.getCurrentVolume()
    }

    fun stopAudio() {
        audioPlayer.stop()
        _audioState.value = AudioState.Idle
        _isMuted.value = false
        _volumeLevel.value = 1f
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}