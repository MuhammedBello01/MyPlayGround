package com.emperormoh.myplayground.presentation.screens.camera_stuffs

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
//import androidx.xr.scenecore.CameraView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun NumberScannerScreen() {
//    var scannedText by remember { mutableStateOf("") }
//    var showCamera by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // TextField to display the scanned number
//        TextField(
//            value = scannedText,
//            onValueChange = { scannedText = it },
//            label = { Text("Scanned Number") }
//        )
//
//        // Button to launch camera
//        Button(
//            onClick = { showCamera = true },
//            modifier = Modifier.padding(top = 16.dp)
//        ) {
//            Icon(Icons.Default.Camera, contentDescription = "Scan")
//            Text(" Scan Number")
//        }
//
//        // Camera Preview (when active)
//        if (showCamera) {
//            CameraView(
//                onTextScanned = { text ->
//                    scannedText = text
//                    showCamera = false
//                },
//                onClose = { showCamera = false }
//            )
//        }
//    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        var scannedText by remember { mutableStateOf("") }
        var isScanning by remember { mutableStateOf(false) }

        IconButton(onClick = { isScanning = true }) {
            Icon(Icons.Default.Camera, contentDescription = "Scan Number")
        }

        if (isScanning) {
            ScanNumberScreen { number ->
                scannedText = number
                isScanning = false
            }
        }

        Text("Detected Number: $scannedText")
    }


}

@Composable
fun CameraView(
    onTextScanned: (String) -> Unit,
    onClose: () -> Unit
) {
    Box{
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val textRecognizer = remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            update = { previewView ->
                val cameraExecutor = ContextCompat.getMainExecutor(context)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    // Set up image analysis (OCR)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor) { imageProxy ->
                                processImage(imageProxy, textRecognizer) { text ->
                                    onTextScanned(text)
                                }
                                imageProxy.close()
                            }
                        }

                    // Bind camera to lifecycle
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                }, cameraExecutor)
            }
        )

        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Close, "Close Camera")
        }
    }

}

@OptIn(ExperimentalGetImage::class)
private fun processImage(
    imageProxy: ImageProxy,
    textRecognizer: TextRecognizer,
    onTextDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image ?: return
    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    textRecognizer.process(image)
        .addOnSuccessListener { result ->
            val scannedNumber = result.text
                .replace("\n", " ") // Clean up line breaks
                .filter { it.isDigit() } // Extract only digits
            onTextDetected(scannedNumber)
        }
        .addOnFailureListener { e ->
            Log.e("OCR", "Text recognition failed", e)
        }
}

@Composable
fun ScanNumberScreen(onNumberDetected: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val imageAnalyzer = remember {
        ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context), PhoneNumberAnalyzer { text ->
                    onNumberDetected(text)
                })
            }
    }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProviderFuture.get().bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            previewView
        },
        modifier = Modifier
            .fillMaxSize()
    )
}
