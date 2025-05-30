package com.emperormoh.myplayground.presentation.componenets.audio_play
//
//import android.util.Log
//import com.google.gson.Gson
//import com.google.gson.JsonSyntaxException
//import com.wemabank.model.aichat.AiChatPayLoad
//import com.wemabank.model.common.AlatWebSocketMessage
//import io.ktor.client.HttpClient
//import io.ktor.client.plugins.websocket.webSocket
//import io.ktor.websocket.Frame
//import io.ktor.websocket.WebSocketSession
//import io.ktor.websocket.close
//import io.ktor.websocket.readReason
//import io.ktor.websocket.readText
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import javax.inject.Inject
//import javax.inject.Singleton
//
class AlatWebSocketManager{

}
//@Singleton
//class AlatWebSocketManager @Inject constructor(
//    private val client: HttpClient
//) {
//    private var webSocketSession: WebSocketSession? = null
//    private var isConnected = false
//    private val gson = Gson()
//
//    // Enhanced message buffering for large data
//    private val messageBuffer = StringBuilder()
//    private var expectedMessageSize: Long? = null
//    private var currentMessageSize: Long = 0
//
//    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
//    val connectionState: StateFlow<ConnectionState> = _connectionState
//
//    private var messageListener: ((AlatWebSocketMessage) -> Unit)? = null
//    private var connectionListener: (() -> Unit)? = null
//    private var streamingProgressListener: ((current: Long, total: Long?) -> Unit)? = null
//
//    sealed class ConnectionState {
//        data object Connected : ConnectionState()
//        data object Connecting : ConnectionState()
//        data object Disconnected : ConnectionState()
//        data class Error(val exception: Throwable) : ConnectionState()
//        data class Streaming(val progress: Float) : ConnectionState()
//    }
//
//    suspend fun connect(
//        url: String,
//        onMessageReceived: (AlatWebSocketMessage) -> Unit,
//        onConnected: (() -> Unit)? = null,
//        onStreamingProgress: ((current: Long, total: Long?) -> Unit)? = null
//    ) {
//        close()
//
//        messageListener = onMessageReceived
//        connectionListener = onConnected
//        streamingProgressListener = onStreamingProgress
//
//        _connectionState.value = ConnectionState.Connecting
//
//        try {
//            client.webSocket(url) {
//                webSocketSession = this
//                Log.i("ALAT_WEBSOCKET_MANAGER", "WebSocket connection established with $url")
//                isConnected = true
//                _connectionState.value = ConnectionState.Connected
//                connectionListener?.invoke()
//
//                for (frame in incoming) {
//                    when (frame) {
//                        is Frame.Text -> {
//                            val message = frame.readText()
//                            Log.d("ALAT_WEBSOCKET_MANAGER", "Text frame received: ${message.length} chars")
//                            Log.d("ALAT_WEBSOCKET_MANAGER", "Text frame received: ${message}")
//                            handleTextMessage(message)
//                        }
//                        is Frame.Binary -> {
//                            val data = frame.data
//                            val message = String(data, Charsets.UTF_8)
//                            Log.d("ALAT_WEBSOCKET_MANAGER", "Binary frame received: ${message.length} chars")
//                            handleTextMessage(message)
//                        }
//                        is Frame.Close -> {
//                            Log.w("ALAT_WEBSOCKET_MANAGER", "Connection closed: ${frame.readReason()?.message}")
//                            _connectionState.value = ConnectionState.Disconnected
//                            isConnected = false
//                        }
//                        else -> Unit
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "Connection error: ${e.message}", e)
//            _connectionState.value = ConnectionState.Error(e)
//            isConnected = false
//            throw e
//        }
//    }
//
//    private fun handleTextMessage(message: String) {
//        try {
//            // Append the new message chunk to buffer
//            messageBuffer.append(message)
//            currentMessageSize += message.length
//
//            // Check if this might be a size indicator (for protocols that send size first)
//            if (expectedMessageSize == null && messageBuffer.length < 100) {
//                val trimmed = messageBuffer.toString().trim()
//                if (trimmed.matches(Regex("\\d+"))) {
//                    // This might be a size indicator
//                    try {
//                        expectedMessageSize = trimmed.toLong()
//                        messageBuffer.clear()
//                        currentMessageSize = 0
//                        Log.d("ALAT_WEBSOCKET_MANAGER", "Expected message size: $expectedMessageSize")
//                        return
//                    } catch (e: NumberFormatException) {
//                        // Not a size indicator, continue with normal processing
//                    }
//                }
//            }
//
//            // Update streaming progress
//            expectedMessageSize?.let { total ->
//                val progress = (currentMessageSize.toFloat() / total.toFloat()).coerceIn(0f, 1f)
//                _connectionState.value = ConnectionState.Streaming(progress)
//                streamingProgressListener?.invoke(currentMessageSize, total)
//            }
//
//            val bufferedContent = messageBuffer.toString()
//
//            // Try to detect complete JSON messages
//            when {
//                isCompleteJsonMessage(bufferedContent) -> {
//                    Log.v("ALAT_WEBSOCKET_MANAGER", "Complete JSON message received: ${bufferedContent.length} chars")
//                    processCompleteMessage(bufferedContent)
//                    resetBuffer()
//                }
//                expectedMessageSize != null && currentMessageSize >= expectedMessageSize!! -> {
//                    Log.v("ALAT_WEBSOCKET_MANAGER", "Expected size reached: $currentMessageSize chars")
//                    processCompleteMessage(bufferedContent)
//                    resetBuffer()
//                }
//                bufferedContent.length > MAX_BUFFER_SIZE -> {
//                    Log.w("ALAT_WEBSOCKET_MANAGER", "Buffer size exceeded maximum, attempting to process")
//                    // Try to process anyway or reset buffer to prevent memory issues
//                    if (isValidJson(bufferedContent)) {
//                        processCompleteMessage(bufferedContent)
//                        resetBuffer()
//                    } else {
//                        Log.e("ALAT_WEBSOCKET_MANAGER", "Buffer overflow with invalid JSON, resetting")
//                        resetBuffer()
//                    }
//                }
//                else -> {
//                    Log.d("ALAT_WEBSOCKET_MANAGER", "Partial message buffered: ${bufferedContent.length} chars")
//                }
//            }
//
//        } catch (e: Exception) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "Error handling message: ${e.message}", e)
//            resetBuffer()
//        }
//    }
//
//    private fun isCompleteJsonMessage(json: String): Boolean {
//        if (json.isBlank()) return false
//
//        val trimmed = json.trim()
//
//        // Check for complete JSON object
//        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
//            return isValidJsonObject(trimmed)
//        }
//
//        // Check for complete JSON array
//        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
//            return isValidJsonArray(trimmed)
//        }
//
//        return false
//    }
//
//    private fun isValidJsonObject(json: String): Boolean {
//        var braceCount = 0
//        var inString = false
//        var escaped = false
//
//        for (char in json) {
//            when {
//                escaped -> escaped = false
//                char == '\\' && inString -> escaped = true
//                char == '"' && !escaped -> inString = !inString
//                !inString && char == '{' -> braceCount++
//                !inString && char == '}' -> braceCount--
//            }
//        }
//
//        return braceCount == 0 && isValidJson(json)
//    }
//
//    private fun isValidJsonArray(json: String): Boolean {
//        var bracketCount = 0
//        var braceCount = 0
//        var inString = false
//        var escaped = false
//
//        for (char in json) {
//            when {
//                escaped -> escaped = false
//                char == '\\' && inString -> escaped = true
//                char == '"' && !escaped -> inString = !inString
//                !inString && char == '[' -> bracketCount++
//                !inString && char == ']' -> bracketCount--
//                !inString && char == '{' -> braceCount++
//                !inString && char == '}' -> braceCount--
//            }
//        }
//
//        return bracketCount == 0 && braceCount == 0 && isValidJson(json)
//    }
//
//    private fun processCompleteMessage(message: String) {
//        try {
//            val parsed = parseMessage(message)
//            if (parsed != null) {
//                _connectionState.value = ConnectionState.Connected // Reset from streaming state
//                messageListener?.invoke(parsed)
//                Log.i("ALAT_WEBSOCKET_MANAGER", "$parsed")
//                Log.i("ALAT_WEBSOCKET_MANAGER", "Message successfully processed and delivered")
//            } else {
//                Log.w("ALAT_WEBSOCKET_MANAGER", "Failed to parse complete message")
//            }
//        } catch (e: Exception) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "Error processing complete message: ${e.message}", e)
//        }
//    }
//
//    private fun resetBuffer() {
//        messageBuffer.clear()
//        expectedMessageSize = null
//        currentMessageSize = 0
//    }
//
//    suspend fun sendMessage(payload: AiChatPayLoad) {
//        if (!isConnected) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "WebSocket is not connected. Message not sent.")
//            return
//        }
//
//        try {
//            val json = convertToJson(payload)
//
//            // For large messages, optionally send size first
//            if (json.length > LARGE_MESSAGE_THRESHOLD) {
//                Log.d("ALAT_WEBSOCKET_MANAGER", "Sending large message: ${json.length} chars")
//                // Uncomment if your protocol supports size prefixing
//                // webSocketSession?.send(Frame.Text(json.length.toString()))
//            }
//
//            webSocketSession?.send(Frame.Text(json))
//            Log.d("ALAT_WEBSOCKET_MANAGER", "Message sent: ${json.length} chars")
//        } catch (e: Exception) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "Error sending message: ${e.message}", e)
//            _connectionState.value = ConnectionState.Error(e)
//            isConnected = false
//            throw e
//        }
//    }
//
//    suspend fun close() {
//        try {
//            webSocketSession?.close()
//            Log.i("ALAT_WEBSOCKET_MANAGER", "Connection closed")
//        } catch (e: Exception) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "Error closing connection: ${e.message}", e)
//        } finally {
//            webSocketSession = null
//            isConnected = false
//            resetBuffer()
//            _connectionState.value = ConnectionState.Disconnected
//            messageListener = null
//            connectionListener = null
//            streamingProgressListener = null
//        }
//    }
//
//    private fun convertToJson(payload: AiChatPayLoad): String {
//        return gson.toJson(payload)
//    }
//
//    private fun parseMessage(text: String): AlatWebSocketMessage? {
//        return try {
//            gson.fromJson(text, AlatWebSocketMessage::class.java)
//        } catch (e: JsonSyntaxException) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "JSON parsing failed: ${e.message}", e)
//            null
//        } catch (e: Exception) {
//            Log.e("ALAT_WEBSOCKET_MANAGER", "Unexpected parsing error: ${e.message}", e)
//            null
//        }
//    }
//
//    private fun isValidJson(json: String): Boolean {
//        return try {
//            gson.fromJson(json, Any::class.java)
//            true
//        } catch (e: JsonSyntaxException) {
//            false
//        } catch (e: Exception) {
//            false
//        }
//    }
//
//    companion object {
//        private const val MAX_BUFFER_SIZE = 10 * 1024 * 1024 // 10MB max buffer
//        private const val LARGE_MESSAGE_THRESHOLD = 1024 * 1024 // 1MB threshold for large messages
//    }
//}


//import io.ktor.client.HttpClient
//import io.ktor.client.engine.okhttp.OkHttp
//import io.ktor.client.plugins.logging.DEFAULT
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logger
//import io.ktor.client.plugins.logging.Logging
//import io.ktor.client.plugins.websocket.WebSockets

//@Provides
//@Singleton
//fun provideHttpClient(): HttpClient {
//    return HttpClient(OkHttp) {
//        install(WebSockets) {
//            maxFrameSize = Long.MAX_VALUE
//        }
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.ALL
//        }
//    }
//}