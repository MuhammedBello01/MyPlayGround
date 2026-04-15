package com.emperormoh.myplayground.presentation.componenets.audio_play.porcupine

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.launch

@Composable
fun VoiceListenerScreen() {
    val context = LocalContext.current
    val activity = context as Activity
    val detector = remember { VoiceWakeWordDetector(context) }

    var isListening by remember { mutableStateOf(false) }
    var hasPermission by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // Flag to know if we should show rationale
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasPermission = isGranted
            if (isGranted) {
                Toast.makeText(context, "Permission granted. Speak now...", Toast.LENGTH_SHORT).show()
                detector.startListening {
                    // Action when wake word detected
                    Log.d("WakeWord", "Hey ALAT Detected!")
                    coroutineScope.launch {
                        showDialog = true
                    }
                }
                isListening = true
            } else {
                Toast.makeText(context, "Microphone permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        val permissionStatus = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            hasPermission = true
            detector.startListening {
                Log.d("WakeWord", "Hey ALAT Detected!")
                coroutineScope.launch {
                    showDialog = true
                }
            }
            isListening = true
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                showRationaleDialog = true
            } else {
                // User permanently denied
                showSettingsDialog = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (isListening) {
                detector.stopListening()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = when {
                !hasPermission -> "Awaiting microphone permission..."
                isListening -> "Listening for 'Hey ALAT'..."
                else -> "Not listening"
            }
        )
        // Popup dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Voice Triggered") },
                text = { Text("You said 'Hey ALAT'") },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
            Log.d("Dialog Trigger", "Dialog trigger>>>>>>>>>>>>>>>>>>!")
        }
    }


    // Rationale Dialog
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Microphone Access Needed") },
            text = { Text("To recognize your voice, we need access to your microphone.") },
            confirmButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }) {
                    Text("Allow")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    // Go to Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Permission Required") },
            text = {
                Text("You've permanently denied the mic permission. Please enable it from app settings.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}


//@Composable
//fun VoiceListenerScreen() {
//    val context = LocalContext.current
//    val detector = remember { VoiceWakeWordDetector(context) }
//
//    var isListening by remember { mutableStateOf(false) }
//    var showDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        detector.startListening {
//            // Trigger something when "Hey ALAT" is detected
//            Log.d("WakeWord", "Hey ALAT Detected!")
//            showDialog = true
//        }
//        isListening = true
//    }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            detector.stopListening()
//        }
//    }
//
//    // UI Text
//    Text(text = if (isListening) "Listening for 'Hey ALAT'..." else "Stopped")
//
//
//}

// Popup dialog
//if (showDialog) {
//    AlertDialog(
//        onDismissRequest = { showDialog = false },
//        title = { Text("Voice Triggered") },
//        text = { Text("You said 'Hey ALAT'") },
//        confirmButton = {
//            TextButton(onClick = { showDialog = false }) {
//                Text("OK")
//            }
//        }
//    )
//}
