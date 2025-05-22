package com.emperormoh.myplayground.presentation.componenets.audio_play

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Base64
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun Base64AudioPlayerWithAudioTrack(
    modifier: Modifier = Modifier,
    base64AudioString: String,
    sampleRate: Int = 44100,
    channelConfig: Int = AudioFormat.CHANNEL_OUT_MONO,
    audioFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
) {
    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var audioData by remember { mutableStateOf<ByteArray?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // Clean up AudioTrack when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            audioTrack?.release()
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Initialize AudioTrack Button
        Button(
            onClick = {
                isLoading = true
                errorMessage = null
                coroutineScope.launch {
                    try {
                        val decodedData = withContext(Dispatchers.IO) {
                            Base64.decode(base64AudioString, Base64.DEFAULT)
                        }

                        audioData = decodedData

                        val bufferSize = AudioTrack.getMinBufferSize(
                            sampleRate,
                            channelConfig,
                            audioFormat
                        )

                        audioTrack = AudioTrack.Builder()
                            .setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build()
                            )
                            .setAudioFormat(
                                AudioFormat.Builder()
                                    .setSampleRate(sampleRate)
                                    .setChannelMask(channelConfig)
                                    .setEncoding(audioFormat)
                                    .build()
                            )
                            .setBufferSizeInBytes(bufferSize)
                            .build()

                        isLoading = false
                    } catch (e: Exception) {
                        errorMessage = "Failed to initialize AudioTrack: ${e.message}"
                        isLoading = false
                    }
                }
            },
            enabled = audioTrack == null && !isLoading
        ) {
            if (isLoading) {
                Text("Initializing...")
            } else {
                Text("Initialize AudioTrack")
            }
        }

        // Play Button
        Button(
            onClick = {
                audioTrack?.let { track ->
                    audioData?.let { data ->
                        coroutineScope.launch {
                            try {
                                withContext(Dispatchers.IO) {
                                    track.play()
                                    isPlaying = true

                                    // Write audio data to AudioTrack
                                    var offset = 0
                                    val chunkSize = 1024

                                    while (offset < data.size && isPlaying) {
                                        val remainingBytes = data.size - offset
                                        val bytesToWrite = minOf(chunkSize, remainingBytes)

                                        val bytesWritten = track.write(
                                            data,
                                            offset,
                                            bytesToWrite
                                        )

                                        if (bytesWritten > 0) {
                                            offset += bytesWritten
                                        } else {
                                            break
                                        }
                                    }

                                    // Wait for playback to finish
                                    while (track.playState == AudioTrack.PLAYSTATE_PLAYING) {
                                        Thread.sleep(100)
                                    }

                                    isPlaying = false
                                }
                            } catch (e: Exception) {
                                errorMessage = "Playback error: ${e.message}"
                                isPlaying = false
                            }
                        }
                    }
                }
            },
            enabled = audioTrack != null && !isPlaying && !isLoading
        ) {
            Text("Play")
        }

        // Stop Button
        Button(
            onClick = {
                audioTrack?.let { track ->
                    try {
                        track.stop()
                        isPlaying = false
                    } catch (e: Exception) {
                        errorMessage = "Stop error: ${e.message}"
                    }
                }
            },
            enabled = audioTrack != null && isPlaying
        ) {
            Text("Stop")
        }

        // Reset Button
        Button(
            onClick = {
                audioTrack?.release()
                audioTrack = null
                audioData = null
                isPlaying = false
                errorMessage = null
            },
            enabled = audioTrack != null && !isPlaying
        ) {
            Text("Reset")
        }

        // Status
        Text(
            text = when {
                isLoading -> "Loading..."
                audioTrack == null -> "Not initialized"
                isPlaying -> "Playing..."
                else -> "Ready to play"
            },
            style = MaterialTheme.typography.bodyMedium
        )

        // Error message
        errorMessage?.let { error ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = "Error: $error",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}

// Alternative implementation for streaming playback
@Composable
fun StreamingBase64AudioPlayer(
    base64AudioString: String,
    sampleRate: Int = 44100,
    modifier: Modifier = Modifier
) {
    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        onDispose {
            audioTrack?.release()
        }
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                if (isPlaying) {
                    audioTrack?.stop()
                    isPlaying = false
                } else {
                    coroutineScope.launch {
                        try {
                            val audioData = withContext(Dispatchers.IO) {
                                Base64.decode(base64AudioString, Base64.DEFAULT)
                            }

                            val bufferSize = AudioTrack.getMinBufferSize(
                                sampleRate,
                                AudioFormat.CHANNEL_OUT_MONO,
                                AudioFormat.ENCODING_PCM_16BIT
                            )

                            audioTrack?.release()
                            audioTrack = AudioTrack(
                                AudioManager.STREAM_MUSIC,
                                sampleRate,
                                AudioFormat.CHANNEL_OUT_MONO,
                                AudioFormat.ENCODING_PCM_16BIT,
                                bufferSize,
                                AudioTrack.MODE_STREAM
                            )

                            audioTrack?.play()
                            isPlaying = true

                            withContext(Dispatchers.IO) {
                                audioTrack?.write(audioData, 0, audioData.size)
                                audioTrack?.stop()
                                isPlaying = false
                            }

                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                            isPlaying = false
                        }
                    }
                }
            }
        ) {
            Text(if (isPlaying) "Stop" else "Play")
        }

        errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

// Usage example
@Composable
fun AudioPlayerScreen2() {
    var selectedPlayer by remember { mutableStateOf(0) }

    // Your base64 PCM audio string
    //val base64AudioString = "your_base64_pcm_audio_data_here"
    val base64AudioString = "BgADAAUACAAIAAcACAAIAAgACgAKAAgACAAGAAUACQAHAAUABQAFAAUABAABAAQABAAGAAUAAgAFAAcACAAIAAkACQAEAAUABQAGAAYABAACAAIAAAABAP//AAABAP//AAD7////AQAGAP//AgD///7/BgAFAAAABAAAAP7///8AAP//AAABAAAA/f/5//7//P/+//3/9//6//3/+P/4//j/+P/2//b/9v/3//f/9f/1//b/9f/3//f/9f/0//b/9v/z//P/8//y//D/8f/u/+v/7f/w//D/7f/u//D/8v/w//D/8f/y//P/8//0//T/9P/x//H/8f/x//H/8v/u/+//8P/z/+z/7v/q/+v/8v/v/+r/7f/s/+v/7P/s/+//8f/y//P/7//x//D/8P/v/+//7//z//D/8v/w//H/8v/0//D/8v/v/+//9P/4//L/8v/t/+v/8P/v/+3/8P/w/+7/8f/w//T/7//w//H/6//z//b/+P/1//j/+v/z//f/9f/6//b/+P/7//f/8v/u//H/8v/7//b/+v/0//L/8v/v/+//+P/2//P/9f/5//j/+//9/wAAAAAAAP3////8//r///8EAAkABAAAAAAA+v/8/wEAAwALAAsACAACAAgAAwANAA0ABQAJABAADwAOAA0ADAAJAAgACAAIAAsADgAWABcAGAAWABUAGgAaABkAGwAfACMAIgAiACEAIAAZABQAEAAVABYAGQAYABYAFQAQAA0ACwATABcAGwAjACkAIgAgACEAJAAlAC4AKgAvADcAMgA3ADEAKQAbABcAGQAbABoAKAAoACQAIQAaAAkAAwD6/wAAEQAjADYAPAA6AC0AHAAZABcAFgAZACMALgArACsAHgAbABMABwANACEALgAyADAAKAAeABMAEgAQABoALwA4ADcAKwAcAAcA+P/4/wMAEwApADkAOgA2ACYAEAAAAPb//f8OACEAMQA1AC0AJAAUABIAEQAiADMANQAvABwA/v/1////EAAaACIAJAAhACAAGwATAAAA5//X/8//xP+//7r/yv/p/xQAOgA/ABsA6P+3/6L/q//M//T/DAAeACsAMQAZAPH/x/+m/5r/nP+e/6D/qv+2/8z/4//7/+z/2P+v/4//ef9m/3f/kP+//9//4P/W/8D/kf+M/7T/5v/4//3/6P/D/5j/ev9+/4j/ev92/3f/fP+K/4D/cv9r/3H/j/+t/7H/iv8w/+r+yP7R/g3/Rf9N/yf/B//8/gr/Rv+c//j/GQDz/7X/pP+1/6n/k/+T/7r//f8rADgADgCt/y3///4a/2D/df9Y/1H/J/8P/1z/CgByACcAkP8I/9b+2v4j/9T/kwD6AJ8A5P+F/3T/lP/U/wsA5v9X/8L+vv5u/4cApgCa/yD/HABbAXYBuwDd/zf/Uf8AAJkAjgDg/6X/7/9nANIA3AAzAOf+Cv4y/h//Y/+A/8MA/QKYAwMBBf4T/dT9Uf9YANAAwgCjAC0Aj//S/1EAGwBn/0H/DgBXACQAPAD5AIwBBAI9Ao8BWwB+/0D/5f7n/iz/AP8b//b/jwBGAOX+Hv1Y/GP+YQJ8BSMFLQIQAF4ARQL6AowCUgKzAnYC9ADe/x0AUACBADkCbAbaCN4ERv7A+mH7tvyg/R3/tADAAXICuAKSAUX/M/3U/Kj+ewHlAp4B3f8WABwCIQR8BAIEvwJWAU4Apf8B/yP+p/6WAJACugLtAT0BCwCf/iP+P/6R/ev8k/3+//ECsgRuBPQCygHaAP3/+v6i/rf+5/4z/8H/LQARAIr/XP+8/zAAjwD1/xv/Gf5U/6kBDAN8A+wCKQLiASQCfwHA/z3+K/5b/6AALQCI/nf9Y/y1/JX/2QHOAIL+cv59/67/7ADGARMCRQNLBDgECgM6Acv++vxJ/XH+Of88/1//bwBeAUEBTAADACIAUf+h/jD/SAA+ALj/AACkAD4BpQHhAbABPwG1AJP/Qv68/Uj+Qf+8/2v/uf4Y/xwA1QBBAWkB+QBwALUAUgGhAT4BegA2ALIAMQHiAMD/f/7S/Gb6Hvm++0r/n/8f/kf+8/8RALP+ov7w/14A4v+C/zQA7wCeAD0BLgMHBGACJACE/83/tv6G/Gz7EfzG/Nv8af49APr/tf13+yv7fvw+/vb/BAH4AFwAWwBRAUMBu/+7/sX+IP+K//r/kwDwAH0BIgGiAD8A1f6H/D37dvyy/8MCcQOSAYD/A/7M/d3+mv88/nH72/o3/PX9Y/50/70AMwEpAJ3/mQAQAf/+p/w//WL/pgArAAoAAAFcASX/dP2j/QD+Kf6x/sj/a/8O/4AAWQFSAEn/af6s/rX/WwEdAlYAh/2f+2P8YP/JABgBjgHfAR0BfP9V/sz9xfzc/cgBJARkAy4Bhf81/4z/nv9uABgChAOTA3YBVf9O/6gARQCR/zL/QP/O/lj9hPwh/t3/IQExAKb+DP4B/noAngNBBkYHJQcWBJIAM/6d/OH7GP62AvIEjwPlAFgAAABH/mb8EP0l/9D/AwHvAtACVABe/jj+rf30/fD9H/5vAGMDcAXEBTMGiAWJA7oBvwDe/279q/nY+Pn7Xf+fAYoDuwQFBc8E8AJnAUL/f/1u/a7+7v70/b39/v5uAF0BQgINAnn/x/xy++f7N/39/sj/RgCkArMFtgavBCgBxP3/+zf9nf8VAXwB3ALLAq0CcQLQAX//Ov1o/L777voO/Lz+0gAPA48EHgYqBDEAX/9lADQCnQAbAP8BFAMNAgsAC/4p/ez9cv8K/3/+0/5k//3+y/0Q/VwAMwKOAsoCKQNqAj0BcAHQAlkGEQaIA1wCKwNrAdz/cf+2/g/9//2n/eH6kfeK9T745f04Ax0EbAOy/5v8GfwkAAAFtQfYCI0H0QNX/5b8pPsg/C7/iwIrBa0EkQCV/LT6+/lo+t39FQGd/jb5+Pf7+koA+APXBV0GwQWZAx8A+vu++EX4tfxcA+YG6wdnBp4DAAKQAPP+iP2L/0YC8gJ3AHL8M/oK+0f9EAFLBK4EMgOkAUQBowL5AQv+ufx7AYkGbAXfApkCngEH/hT5Pvg3+hT80/ww/6wCFwNpAen/OP/eATEFhgbnAxQA6Pxq+039VAEvBRYI7AaNBPgB8P4s+574bPur/h4B8QENArcAWv68/fH+IAH/AhIDTgFmAUUBwQAeARUDNgJYAL3+NP44/h/+s/6SADQEhgdcCcQJNQdqApr9Nvn593/40foW+zL7H//tBM0GDQdwBHsBP/3w+Zn5wfzIA9cHqgnfCcQGyAFw/bD5gfmS/ecDzgTVAvIBFP6H+aP1r/i7/WsCJgKcANoBiAI4AY3/kf9yACsCUAOzAjgB6P+J/aD8qv8oAwgExQN1Avf/9fun+qD5dfpE/fX+8QDSBBcGOAWrA8UCJgK1AFQAov3W+o/5d/fB9tT5xf7zAYYDLwRWAfD+8//u/ez6n/xmABIFHgVEBGUDxgEIAJn91v3E/cT6RvmN+lL9FQD4APYC4gSLBXgC/fvy+WP6N/uC/DH+xwGaAx0DiQOsA1MDvAG1/1H+JP60/pX9dv1VATwFgQY1Br8GIgYWAvn6kPZJ9976fvyI/AH/YQJaA1UERQWrBO8CHgCY/v3+LP9S/Yf8xADGA/wCTgDl/uD+dP82/eT6avio9yj3gvia/CoBHQUxBYgEkgZQCKUGyAK2AS4EZAQnBAkCywGlAff+gP5MAGEA7viQ7vvrKfAN9jP5Y/shAUMGCgh8BxkHpQXbA1QCXQUaCNAI5wJH+876rfzZ/3gAOQHwABj+Tfkd9KPykPPt9RH9QQW7CQcLMgsQCigISwV7A5kAZf+E/Hv4DvVe9FX3TvxuA1AG5AQ6Al3/kPxl+/j8Bv8Z/zP/vQBFBcoIJQuUCyAKZwUS/0H6oPly+bH4pvcq+t7+XgF4AvwCEARCA2kCCQFLAHH/0PtV+4H9ZAFTA4cF1AeWBuUCq/08+kb5rfrr+d/4HvwEATACmgHCAXkDaQRNApD+GP5I/2n+B/4s/lkAd/8Z/qr9df7J/i7/TAF/AcEAVv6V/PL7X/0dAVcEmwWxBCYCP/8S/lv+X/5l/00CvgMqBZ4EoAI6/yD9V/wF/eD/3wAtAL7+fv53/sf+EgGSA4cESwTUAu4AN/5Y/U39Tf2S/48B2AKQAt4C9QKvBLAHZAjbBikDPf17+Jf2+PZ8+D38agH0BQUGLwUBBjAGwgPdAgUEsAJ//lb5SPgp+gD8YPzw/zkF2waXBSsDJQAq/n/8r/vR/Jr+v/7V/k3/VADVAqEFXga4BNoCM/9f+wT4Wfef+BX8+/+9AnME+gNBAvIAUAJHBLsD9QEb/xn9OvuN++/9qAG9BQoI5AflBEABkP2D+9T6ovlA+mT8bv2T/uz/vAEGAywF2QSmAv//of7y/V/8BfuN/OX/AwP8BXoI3AleCvoHpgQ0AO/6bfTr8iD1r/fv+/T+9gDu/3EA/AFQA6UC/f+J/QD9RPzV+bX58vsu/kAAdATeB+UIegaUAnX/W/7C/uX8MvtF/TcAqQE/AdkBLAFpAlQBy/7c/a38bvnA9iz3afo6/Cb+zv9wAZwB6QBFAVIBHgBh/f/7of2I/yT/mf2K/U3/of9P/17+if1s/Nb5ffkR+6P9av5CAAIB1wJUA6oB/P87/rP7pfoX+xb8n/t5+nX8aQGRBAAFbQX3BDIFOwPg/87/gQFaAjoD1gTTBVsFagRQBA4FZgZ3BacEEAI5AIn8DPxK/0UCbQQJBT8EnAMaA1kC6AOUBTUGqQb5Bs0FjQQ4BOAD6AVsCAwJ2QpmCvAH8AShA9kClAG6/hL91PtF/JD86/3uAKcDcQWuBeUGFQf+BXECdgDj/UP9a/o0+Y34W/gc+Pv3FPn4+cn5dPc+9sT16vSZ8uDvnO6X7SftmO7V8T30qvf1+Cn78vxn/Hz60PlQ+Rb4Lvdr9h/3lPgq+aD65PzM/jP/bv4E/cP8YPz7+hT7nPoD+238Av9dABIEqwYQCPsH4AhRCLoIDAkSCKAHBwmVCsUL/gwkCxAKpgmWCqIJnwopCnAKegv0DMAOKxK0FA0UDRSaFMkUERV6FRcTIRGHDtEJngfLBH4DVQJdBOkH+Aj/DR4PphF9EOcQxA7jDTcMrwVvARn+Fvv/9Tf10vQR91H5N/vM+wT78PoX90L25/WD9H3x8O7466DrP+u2627r0Ox37eHtNO1e7XDsgerr6l3sB+6B7tHvPO8Y8cT01fZ8+hT/cAGZAfIBZ/8KANn+SP2z+236OPr/9xL2YPU39f30nPTm80/07fSV9SL1NfZ8+FX6A/wE/VkA7APBB6IKkQsJDXoNbA2FDqAPdg9pDpEO7A2uDW0MqApcCvALsgo/CnoLXQ3LD1cPbxJXE04VkRQoEmsSrhMSE/YQnhHvEewRPhAFDvgMkQ5aDj4MAAzFC0wLewjeBfMEKwVDBWQD4AIaBCIEfQFd/0X9lfu++TX2lvRN8zXwEesW6Gfla+Md4rnh5eVO64bumO4d8M/yE/aE+ST9JQAzAisAH/zz+Yj5nvol+pf6afrN+YH3n/OY8BDxJvIO873zl/IN8j/wF/D38K3zLfZR9x744/if+cP5o/oD/Cj9Rv2P/If7w/tW/NT9CABKA4QG3Qg0C+gN3g3DD2sPFgznB/cCfv70/Hr/rwBlAz4I0Qw8EY0W4BdfGHIZ/BjMF4EXCBesEbYNcQpvCVwJUApiCScKlwvkCqoJngj2CGYGFwbQA9EEqQUqBssFbwb/BhQHfQZ+BdgDggDk/pn6pvgM9SnwSusN5/DjoeEh4NPetN744MnkWOgi7sryefaw+aj9PQGdAy4EmwFy/p/9Lvwi+s34LfdN9p32H/hb+Gr5q/kz+K72pfbc9WLzcfF37hbtpe0T7zDx0PSE+Rj8CP80Au0DbQSDBFcDigPQA6ADUwJOAoICRwL1BFIHzgqKDJwMtgt0Co8IMwfyBGEFnAVmBpAHiglbDNAODxGsElsTCxQHFdoTgxOHEKYOfw0YDhgOxg1hDQgM/wt2DJIM9AtkC+kI1wcpBqEEqwMqAzEDnQQtBYkF0AZZB7UHBgceBQUCB/5O+V3zbe4j68rmieR14aDe/d173vPi9OlO8gn4iPua/SD/3P+rABkBswCSADb+RvvE+FH31fd1+Vn8sv4RAMUAmf84/jX+2/3N+1P40/Ke7o/riOre6f/qdu0z7wHyk/ZS+sD/kwE1BOEFaAj4B08GZgXmBb4GmgfeBwcI+wnZCD4JmwjMCfQJPQhzB0oG/wXUBQgHnwgwDXMQahIpFMMUnBTbEikT0hKIE/"


    Column {
        // Player selection
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Button(
                onClick = { selectedPlayer = 0 },
                colors = if (selectedPlayer == 0) ButtonDefaults.filledTonalButtonColors()
                else ButtonDefaults.outlinedButtonColors()
            ) {
                Text("Full Control")
            }
            Button(
                onClick = { selectedPlayer = 1 },
                colors = if (selectedPlayer == 1) ButtonDefaults.filledTonalButtonColors()
                else ButtonDefaults.outlinedButtonColors()
            ) {
                Text("Simple Streaming")
            }
        }

        when (selectedPlayer) {
            0 -> Base64AudioPlayerWithAudioTrack(
                base64AudioString = base64AudioString,
                modifier = Modifier.fillMaxSize()
            )
            1 -> StreamingBase64AudioPlayer(
                base64AudioString = base64AudioString,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}