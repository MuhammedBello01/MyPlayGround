package com.emperormoh.myplayground

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class TFLiteModelLoader(context: Context) {
    private var interpreter: Interpreter? = null

    init {
        //loadModel(context)
        val modelPath = "network_provider_model.tflite"
        val assetManager = context.assets
        val modelBytes = assetManager.open(modelPath).readBytes()
        val byteBuffer = ByteBuffer.allocateDirect(modelBytes.size).apply {
            order(ByteOrder.nativeOrder())
            put(modelBytes)
        }

        val options = Interpreter.Options()
        interpreter = Interpreter(byteBuffer, options)
    }

    private fun loadModel(context: Context) {
        try {
            val assetManager = context.assets
            val modelInputStream: InputStream = assetManager.open("network_provider_model.tflite")

            val modelFile = File(context.filesDir, "model.tflite")
            val outputStream = FileOutputStream(modelFile)
            modelInputStream.copyTo(outputStream)
            modelInputStream.close()
            outputStream.close()

            val modelBuffer = modelFile.readBytes().let {
                ByteBuffer.allocateDirect(it.size).apply {
                    order(ByteOrder.nativeOrder())
                    put(it)
                    rewind()
                }
            }

            val options = Interpreter.Options()
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: Exception) {
            Log.e("TFLiteModelLoader", "Error loading model: ${e.message}")
        }
    }

    fun predict(phoneNumber: String): Pair<Int, Float>? {
        if (interpreter == null) {
            Log.e("TFLiteModelLoader", "Model is not initialized")
            return null
        }

        if (phoneNumber.length != 11) {
            Log.e("TFLiteModelLoader", "Invalid phone number length")
            return null
        }

        val inputArray = preprocessPhoneNumber(phoneNumber)
        val outputArray = Array(1) { FloatArray(4) } // Change 3 -> 4

        interpreter?.run(inputArray, outputArray)

        Log.d("TFLiteModelLoader", "Model output: ${outputArray[0].toList()}")

        val predictedIndex = outputArray[0].indices.maxByOrNull { outputArray[0][it] } ?: -1
        val confidence = outputArray[0][predictedIndex]

        return Pair(predictedIndex, confidence)
    }

    private fun preprocessPhoneNumber(phoneNumber: String): FloatArray {
        return phoneNumber.map { it.toString().toFloat() / 9.0f }.toFloatArray()
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}