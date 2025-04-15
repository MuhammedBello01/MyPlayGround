package com.emperormoh.myplayground.presentation.componenets.alat_components

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.Locale

class SpeechToTextViewModel : ViewModel() {
//    private val _spokenText = mutableStateOf("")
//    val spokenText: State<String> = _spokenText
    val spokenText = mutableStateOf("")

    fun updateSpokenText(newText: String) {
       // _spokenText.value = newText
        spokenText.value = newText
    }

    private var speechRecognizer: SpeechRecognizer? = null

    fun startListening(context: Context, onResult: (String) -> Unit, onDone: () -> Unit) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                   // _spokenText.value = matches?.firstOrNull() ?: ""
                    onResult(matches ?: "")
                    onDone()
                }

                override fun onError(error: Int) {
                    val errorMessage = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission error"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech timeout"
                        else -> "Error: $error"
                    }
                    //_spokenText.value = "Error code: $error"
                    spokenText.value = "Error code: $error"
                    Log.e("SpeechRecognizer", errorMessage)
                    onDone()
                }

                // Required overrides (can be empty)
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        speechRecognizer?.startListening(intent)
    }

    override fun onCleared() {
        speechRecognizer?.destroy()
        super.onCleared()
    }
}
