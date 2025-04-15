package com.emperormoh.myplayground.presentation.componenets

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.emperormoh.myplayground.presentation.componenets.alat_components.SpeechToTextViewModel
import com.emperormoh.myplayground.presentation.screens.transfer.SpaceHeight

@Composable
fun SpeechInputTextField(viewModel: SpeechToTextViewModel) {
    val context = LocalContext.current
    val activity = context as Activity
    var spokenText by viewModel.spokenText
    var isListening by remember { mutableStateOf(false) }

    // Flag to know if we should show rationale
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Launcher to request mic permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission granted. Speak now...", Toast.LENGTH_SHORT).show()
            viewModel.startListening(
                context,
                onResult = { spokenText = it },
                onDone = { isListening = false }
            )
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                showRationaleDialog = true
            } else {
                // User permanently denied
                showSettingsDialog = true
            }
        }
    }


    Column(modifier = Modifier.padding(16.dp)) {
        SpaceHeight(50.dp)
        OutlinedTextField(
            value = spokenText,
            onValueChange = { newValue -> viewModel.updateSpokenText(newValue) },
            label = { Text("Say something...") },
            trailingIcon = {
                IconButton(onClick = {
                    if (isMicPermissionGranted(context)){
                        isListening = true
                        viewModel.startListening(
                            context = context,
                            onResult =  { viewModel.updateSpokenText(it) },
                            onDone = { isListening = false}
                        )
                        Toast.makeText(context, "Speak now...", Toast.LENGTH_SHORT).show()
                    }else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = "Start Recording")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (isListening) {
            Text("Listening...", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 8.dp))
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

fun isMicPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED
}

