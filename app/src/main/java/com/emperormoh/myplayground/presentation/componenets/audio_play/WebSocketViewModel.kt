package com.emperormoh.myplayground.presentation.componenets.audio_play

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.update

//
//import android.content.Context
//import android.util.Log
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.gson.Gson
//import com.google.gson.stream.JsonReader
//import com.wemabank.core.data.AuthManager
//import com.wemabank.core.ui.utils.PCM16AudioPlayer
//import com.wemabank.model.aichat.AiChatPayLoad
//import com.wemabank.model.aichat.AiChatRequestType
//import com.wemabank.model.aichat.AiChatResponse
//import com.wemabank.model.aichat.AiChatResponseType
//import com.wemabank.model.common.AlatWebSocketMessage
//import com.wemabank.network.utils.AlatWebSocketManager
//import com.wemabank.network.utils.SpeechRecognizerManager
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.filter
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import java.io.StringReader
//import java.util.concurrent.atomic.AtomicBoolean
//import javax.inject.Inject
//

class WebSocketViewModel{

}
//@HiltViewModel
//class WebSocketViewModel @Inject constructor(
//    private val webSocketManager: AlatWebSocketManager,
//    private val speechRecognizerManager: SpeechRecognizerManager,
//    private val authManager: AuthManager,
//) : ViewModel() {
//
////    private val _messages = mutableStateListOf<AlatWebSocketMessage>()
////    val messages: List<AlatWebSocketMessage> = _messages
//
////    val connectionState = webSocketManager.connectionState
////        .stateIn(
////            scope = viewModelScope,
////            started = SharingStarted.WhileSubscribed(5000),
////            initialValue = AlatWebSocketManager.ConnectionState.Disconnected
////        )
//
//    val spokenText = mutableStateOf("")
//    var isProcessing = mutableStateOf(true)
//    private val _uiState = MutableStateFlow(AiAssistantUiState())
//    val uiState: StateFlow<AiAssistantUiState> = _uiState.asStateFlow()
//    private val _messages = MutableStateFlow<List<ChatModel>>(emptyList())
//    val messages: StateFlow<List<ChatModel>> = _messages.asStateFlow()
//
//    private var conversationId : String = ""
//    private var pillarValue: Int = 0
//
//    private var audioPlayer: PCM16AudioPlayer? = null
//
//
//    private val _formFields = MutableStateFlow<Map<String, String>>(emptyMap())
//    private val email = authManager.getCurrentUser()?.emailAddress ?: ""
//    private val token = authManager.getCurrentUser()?.token?.accessToken ?: ""
//    private val profileId = authManager.getCurrentUser()?.profileID ?: ""
//    private val cif = authManager.getCurrentUser()?.cif ?: ""
//    private var hasSentPreflight = AtomicBoolean(false)
//
//
//    var isOnboarding : Boolean = false
//    var messageToLog: String = ""
//    private val messageBuffer = StringBuilder()
//    private var openBraces = 0
//    private var inMessage = false
//
//    fun initAudioPlayer(context: Context){
//        audioPlayer = PCM16AudioPlayer(context)
//    }
//
//    private var reconnectJob: Job? = null
//
//    fun connectWebSocket() {
//        hasSentPreflight.set(false)
//        val baseUrl = "wss://alatnotificationroute-alat-two.apps.alatarodev.westeurope.aroapp.io/api/v1/ws/connect/$cif"
//        viewModelScope.launch {
//            webSocketManager.connect(
//                url = baseUrl,
//                onMessageReceived = { msg ->
//                    viewModelScope.launch {
//                    //val data = msg.data.toString()
//
////                    messageBuffer.append(data)
////                    val fullMessage = messageBuffer.toString().trim()
//                        Log.e("RAW_MESSAGE", "MESSAGE: ${msg.data}")
//                    val message = parseDataToAiChatResponseSimple(msg.data)
//
//                    message?.let {
//                        messageToLog = message.toString()
//                        isProcessing.value = it.type == AiChatResponseType.ACK.value
//
//                        when (it.type) {
//                            AiChatResponseType.TEXT.value -> {
//                                _messages.update { currentMessages -> currentMessages + ChatModel(message = it.message, isUser = false) } }
//                            AiChatResponseType.VOICE.value -> {
//                                _messages.update { currentMessages ->
//                                    currentMessages + ChatModel(message = it.message, isUser = false, audioData = it.audio_data, messageType = AiChatResponseType.VOICE.value)
//                                }
//                            }
//                        }
//                        conversationId = it.conversation_id
//                    }
//                }
//                    Log.e("WebsocketConnect Connected", "MESSAGE: $messageToLog")
//                },
//                onConnected = {
//                    // Send any initial/preflight messages here
//                    viewModelScope.launch { sendPreflightMessage() }
//                }
//            )
//           // setupReconnect()
//        }
//    }
//
////    fun sendMessage(payload: AiChatPayLoad) {
////        viewModelScope.launch {
////            webSocketManager.sendMessage(payload)
////        }
////    }
//
//    suspend fun sendMessage(message: String) {
//        val request = AiChatPayLoad(
//            message = message,
//            conversation_id = conversationId,
//            type = AiChatRequestType.MESSAGE.value,
//            pillar = pillarValue,
//            queryParams =  _formFields.value
//        )
//        webSocketManager.sendMessage(request)
//        _messages.update { currentMessages ->
//            currentMessages + ChatModel(message = message, isUser = true)
//        }
//    }
//
//    private suspend fun sendPreflightMessage() {
//        if (hasSentPreflight.compareAndSet(false, true)) {
//            val preflightMessage = AiChatPayLoad(
//                type = AiChatRequestType.AUTH.value,
//                email = email,
//                pillar = 1,
//                jwt = token
//            )
//            webSocketManager.sendMessage(preflightMessage)
//        }else {
//            Log.e("Preflight", "Skipped - already sent")
//        }
//    }
//
//    fun updateFormField(fieldName: String, value: String) {
//        // Update the form fields map
//        viewModelScope.launch {
//            val updatedFields = _formFields.value.toMutableMap()
//            if (value.isNotBlank()) {
//                updatedFields[fieldName] = value
//            } else {
//                updatedFields.remove(fieldName)
//            }
//            _formFields.value = updatedFields
//        }
//    }
//
//    fun setPillar(pillar: Int){
//        pillarValue = pillar
//    }
//
//    fun startListening(context: Context, onResult: (String) -> Unit, onDone: () -> Unit, onError: (String) -> Unit) {
//        speechRecognizerManager.startListening(
//            context,
//            onResult = {
//                spokenText.value = it
//                onResult(it)
//            },
//            onDone = {
//                onDone()
//            },
//            onError = { errorMessage ->
//                onError(errorMessage)
//                Log.e("SpeechRecognizer error", errorMessage)
//            }
//        )
//    }
//
//    fun updateSpokenText(newText: String) {
//        spokenText.value = newText
//    }
//
//    fun updateChat(value: String){
//        _uiState.update {
//            it.copy(
//                requestMessage = value,
//                typing = ""
//            )
//        }
//        spokenText.value = ""
//    }
//
//    fun onChatValueChanged(value: String){
//        _uiState.update {
//            it.copy(
//                typing = value
//            )
//        }
//    }
//
//    private fun setupReconnect() {
//        reconnectJob?.cancel()
//        reconnectJob = viewModelScope.launch {
//            webSocketManager.connectionState
//                .filter { it is AlatWebSocketManager.ConnectionState.Error ||
//                        it is AlatWebSocketManager.ConnectionState.Disconnected }
//                .collectLatest {
//                    delay(5000)
//                    connectWebSocket()
//                }
//        }
//    }
//
////    private fun parseMessage(jsonString: String): AiChatResponse? {
////        val reader = JsonReader(StringReader(jsonString))
////        reader.isLenient = true // Allows for more flexible JSON parsing
////        Log.e("MESSAGE_TO_PASS", "connectWebSocket: $jsonString")
////        return try {
////            val response = parseAiChatResponse(reader)
////            messageBuffer.clear() // Clear your buffer after successful parsing
////            response
////        } catch (e: Exception) {
////            Log.e("AiAssistant", "JSON parsing failed", e)
////            null
////        } finally {
////            reader.close()
////        }
////    }
//
//    private fun parseDataToAiChatResponseSimple(data: Any?): AiChatResponse? {
//        return try {
//            val gson = Gson()
//            // Convert Any? to JsonElement, then to AiChatResponse
//            val jsonElement = gson.toJsonTree(data)
//            gson.fromJson(jsonElement, AiChatResponse::class.java)
//        } catch (e: Exception) {
//            Log.e("AiAssistant", "Failed to parse data to AiChatResponse", e)
//            null
//        }
//    }
//
//
//    private fun parseAiChatResponse(reader: JsonReader): AiChatResponse {
//        var type = 0
//        var message = ""
//        var conversationId = ""
//        var timestamp = ""
//        var audioData = ""
//
//        reader.beginObject()
//        while (reader.hasNext()) {
//            when (reader.nextName()) {
//                "type" -> type = reader.nextInt()
//                "message" -> message = reader.nextString()
//                "conversation_id" -> conversationId = reader.nextString()
//                "timestamp" -> timestamp = reader.nextString()
//                "audio_data" -> audioData = reader.nextString()
//                else -> reader.skipValue() // Skip any unexpected fields
//            }
//        }
//        reader.endObject()
//
//        return AiChatResponse(
//            type = type,
//            message = message,
//            conversation_id = conversationId,
//            timestamp = timestamp,
//            audio_data = audioData
//        )
//    }
//
//    fun playAudio(audioData: String){
//        audioPlayer?.playPCM16FromString(audioData)
//    }
//
//    fun stopAudio(){
//        audioPlayer?.stop()
//    }
//
//    fun toggleMute(){
//        audioPlayer?.toggleMute()
//    }
//
//
//     fun disconnectWebSocket() {
//        if (_messages.value.isNotEmpty()) {
//            clearMessages()
//        }
//        viewModelScope.launch {
//            webSocketManager.close()
//            reconnectJob?.cancel()
//        }
//    }
//
//    override fun onCleared() {
//        speechRecognizerManager.destroy()
//        audioPlayer?.stop()
//        disconnectWebSocket()
//        super.onCleared()
//    }
//
//    private fun clearMessages() {
//        _messages.update { emptyList() }  // Clears and notifies observers
//    }
//
//}

//ktorClientCio = "2.3.7"
//ktorClientCore = "2.3.7"
//ktorClientLogging = "2.3.7"
//ktorClientOkhttp = "2.3.7"
//ktorClientWebsockets = "2.3.7"
//ktor-client-cio = { module = "io.ktor:ktor-client-cio", version.ref = "ktorClientCio" }
//ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktorClientCore" }
//ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktorClientLogging" }
//ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktorClientOkhttp" }
//ktor-client-websockets = { module = "io.ktor:ktor-client-websockets", version.ref = "ktorClientWebsockets" }
//implementation(libs.ktor.client.core)
////implementation(libs.ktor.client.cio)
//implementation(libs.ktor.client.okhttp)
//
//implementation(libs.ktor.client.websockets)
//implementation(libs.ktor.client.logging)

//
//private val messageBuffer = StringBuilder()
//private var openBraces = 0
//private var inMessage = false
//fun connectWebSocket2() {
//    hasSentPreflight.set(false)
//    val baseUrl =
//        "wss://alatnotificationroute-alat-two.apps.alatarodev.westeurope.aroapp.io/api/v1/ws/connect/$cif"
//
//    webSocketManager.connect(
//        url = baseUrl,
//        onMessageReceived = { msg ->
//            viewModelScope.launch {
//                val chunk = msg.data.toString()
//
//                // Process each character to track JSON object boundaries
//                for (char in chunk) {
//                    if (char == '{') {
//                        openBraces++
//                        inMessage = true
//                    } else if (char == '}') {
//                        openBraces--
//                    }
//
//                    if (inMessage) {
//                        messageBuffer.append(char)
//                    }
//
//                    // If JSON object seems complete
//                    if (inMessage && openBraces == 0) {
//                        val fullMessage = messageBuffer.toString()
//
//                        try {
//                            val message =
//                                Gson().fromJson(fullMessage, AiChatResponse::class.java)
//                            messageToLog = message.message
//
//                            when (message.type) {
//                                AiChatResponseType.TEXT.value -> _messages.update {
//                                    it + ChatModel(message = message.message, isUser = false)
//                                }
//
//                                AiChatResponseType.VOICE.value -> _messages.update {
//                                    it + ChatModel(
//                                        message = message.message,
//                                        isUser = false,
//                                        audioData = message.audio_data,
//                                        messageType = AiChatResponseType.VOICE.value
//                                    )
//                                }
//                            }
//
//                            conversationId = message.conversation_id.toString()
//                        } catch (e: Exception) {
//                            Log.e("AiAssistant", "Parsing failed for: $fullMessage", e)
//                        }
//
//                        // Reset buffer for next message
//                        messageBuffer.clear()
//                        inMessage = false
//                    }
//                }
//            }
//
//            Log.e("WebsocketConnect Connected", "MESSAGE: $messageToLog")
//        },
//        onConnected = {
//            viewModelScope.launch { sendPreflightMessage() }
//        }
//    )
//}

//    private fun parseMessage(text: String): AiChatResponse? {
//        //return Gson().fromJson(text, AiChatResponse::class.java)
//        return try {
//            val result = Gson().fromJson(text, AiChatResponse::class.java)
//            messageBuffer.clear()
//            result ?: run {
//                Log.e("AiAssistant", "parseMessage: fromJson returned null for input: $text")
//                null
//            }
//        } catch (e: Exception) {
//            Log.e("AiAssistant", "parseMessage: JSON parsing failed", e)
//            null
//        }
//    }