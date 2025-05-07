package com.emperormoh.myplayground.presentation

import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechExample() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("Hello, Jetpack Compose!") }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    // Initialize TTS
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context, OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US // Set language (e.g., US English)
            }
        })
    }

    // Clean up when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text to speak") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        ) {
            Text("Speak")
        }
    }
}