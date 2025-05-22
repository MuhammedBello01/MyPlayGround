package com.emperormoh.myplayground.presentation.componenets.audio_play

import android.util.Base64
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Composable
fun AudioPlayerScreen() {
    val context = LocalContext.current
    val viewModel: AudioViewModel = viewModel { AudioViewModel(context) }
    val audioState by viewModel.audioState.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val volumeLevel by viewModel.volumeLevel.collectAsState()
    val playbackComplete by viewModel.playbackComplete.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Show playback completion message
        if (playbackComplete) {
            Text(
                text = "Playback completed successfully!",
                color = Color.Green,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        when (audioState) {
            is AudioState.Idle -> {
                Button(
                    onClick = {
                        viewModel.fetchAndPlayAudio("https://your-api-endpoint.com/audio")
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Fetch and Play from Network")
                }

                Button(
                    onClick = {
                        val base64PcmData = generate440HzPCMBase64()//"""
//                            /v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A
//                            /v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A
//                            /v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A
//                            /v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A/v8A
//                            """.trimIndent().replace("\n", "")

                        Log.e("Test Data", "AudioPlayerScreen: $base64PcmData")

                        viewModel.playLocalString(base64PcmData)
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Play Test Base64 Sample")
                }
            }

            is AudioState.Loading -> {
                CircularProgressIndicator()
                Text("Loading audio...", modifier = Modifier.padding(16.dp))
            }

            is AudioState.Playing -> {
                Text("Now Playing", modifier = Modifier.padding(16.dp))

                // Volume control
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Slider(
                        value = volumeLevel,
                        onValueChange = { viewModel.setVolume(it) },
                        valueRange = 0f..1f,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Text("Volume: ${(volumeLevel * 100).toInt()}%")
                }

                // Control buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.toggleMute() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(if (isMuted) "Unmute" else "Mute")
                    }

                    Button(
                        onClick = { viewModel.stopAudio() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Stop")
                    }
                }
            }

            is AudioState.Error -> {
                Text(
                    text = "Error: ${(audioState as AudioState.Error).message}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = { viewModel.stopAudio() },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Try Again")
                }
            }
        }
    }
}

fun generate440HzPCMBase64(durationSec: Int = 5, sampleRate: Int = 44100): String {
    val numSamples = durationSec * sampleRate
    val samples = ShortArray(numSamples)
    val frequency = 440.0 // A4 tone
    for (i in samples.indices) {
        val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
        samples[i] = (Math.sin(angle) * Short.MAX_VALUE).toInt().toShort()
    }

    // Convert to byte array (Little Endian)
    val byteBuffer = ByteBuffer.allocate(samples.size * 2).order(ByteOrder.LITTLE_ENDIAN)
    for (sample in samples) {
        byteBuffer.putShort(sample)
    }

    return Base64.encodeToString(byteBuffer.array(), Base64.NO_WRAP)
}
