package com.emperormoh.myplayground.presentation.screens.camera_stuffs

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class PhoneNumberAnalyzer(private val onTextFound: (String) -> Unit) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val number = visionText.textBlocks
                    .flatMap { it.lines }
                    .joinToString(" ") { it.text }
                    .filter { it.isDigit() || it.isWhitespace() }

                if (number.isNotBlank()) {
                    onTextFound(number.trim())
                }
            }
            .addOnFailureListener {
                Log.e("OCR", "Failed to recognize text: ${it.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
