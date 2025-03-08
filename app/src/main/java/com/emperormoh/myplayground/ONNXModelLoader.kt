package com.emperormoh.myplayground

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import java.nio.FloatBuffer

class ONNXModelLoader(context: Context) {
    private var ortEnvironment: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var ortSession: OrtSession

    init {
        val modelPath = "network_provider_model.onnx"
        val assetManager = context.assets
        val modelBytes = assetManager.open(modelPath).readBytes()

        val sessionOptions = OrtSession.SessionOptions()
        ortSession = ortEnvironment.createSession(modelBytes, sessionOptions)
    }

    fun predict(inputArray: FloatArray): Pair<Int, Float> {
        val shape = longArrayOf(1, 11)
        val floatBuffer = FloatBuffer.wrap(inputArray)
        val inputTensor = OnnxTensor.createTensor(ortEnvironment, floatBuffer, shape)

        val output = ortSession.run(mapOf("input" to inputTensor))
        val resultArray = (output[0].value as Array<FloatArray>)[0]

        val predictedIndex = resultArray.indices.maxByOrNull { resultArray[it] } ?: -1
        val confidence = resultArray[predictedIndex]

        return Pair(predictedIndex, confidence)
    }

}

// Activity for Model Usage
//val modelLoader = ONNXModelLoader(context)
//
//// Method to Convert Full 11-Digit Phone Number to Input Array
//fun preprocessPhoneNumber(phoneNumber: String): FloatArray {
//    return phoneNumber.map { it.toString().toFloat() / 9.0f }.toFloatArray()
//}
//
//// Example Usage
//val inputArray = preprocessPhoneNumber("08031234567") // Scaled 11-digit input
//val (predictedClass, confidence) = modelLoader.predict(inputArray)
//println("Predicted Class: $predictedClass, Confidence: $confidence")

