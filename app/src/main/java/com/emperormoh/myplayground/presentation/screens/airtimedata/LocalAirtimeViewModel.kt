package com.emperormoh.myplayground.presentation.screens.airtimedata

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.emperormoh.myplayground.ONNXModelLoader
import com.emperormoh.myplayground.presentation.screens.transfer.LocalTransferUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalAirtimeViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LocalAirtimeUiState())
    val uiState: StateFlow<LocalAirtimeUiState> = _uiState.asStateFlow()

    private var modelLoader: ONNXModelLoader? = null

    fun predictPhoneNetwork(phoneNumber: String): Networks{
        try {
            val inputArray = preprocessPhoneNumber(phoneNumber) // Scaled 11-digit input
            val (predictedClass, confidence) = modelLoader?.predict(inputArray)!!
            println("Predicted Class: $predictedClass, Confidence: $confidence")
            _uiState.update {
                it.copy(
                    isPhoneNumberPredictionLoading = false,
                    isPhoneNumberPredicted = true,
                    predictedNetworkIndex = predictedClass
                )
            }
            return when(predictedClass){
                Networks.NineMobile.type -> Networks.NineMobile
                Networks.Airtel.type -> Networks.Airtel
                Networks.Glo.type -> Networks.Glo
                Networks.MTN.type -> Networks.MTN
                else -> Networks.MTN
            }
        }catch (e: Exception){
            _uiState.update {
                it.copy(
                    isPhoneNumberPredictionLoading = false,
                    isPhoneNumberPredicted = true,
                    predictedNetworkIndex =  Networks.MTN.type
                )
            }
            Log.e("PredictNetwork", "Error during network prediction: ${e.message}", e)
            return Networks.MTN
        }
    }

    // Method to Convert Full 11-Digit Phone Number to Input Array
    private fun preprocessPhoneNumber(phoneNumber: String): FloatArray {
        return phoneNumber.map { it.toString().toFloat() / 9.0f }.toFloatArray()
    }

    fun InitModel(context: Context){
         modelLoader = ONNXModelLoader(context)
    }

    fun onPhoneNumberChanged(phoneNumber: String){
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                isPhoneNumberPredictionLoading = false,
                isPhoneNumberPredicted = false,
                predictedNetworkIndex =  Networks.MTN.type
            )
        }
    }

}

data class LocalAirtimeUiState(
    var phoneNumber: String = "",
    val isPhoneNumberPredicted: Boolean = false,
    val isPhoneNumberPredictionLoading: Boolean = false,
    var selectedNetwork: String = "",
    val predictedNetworkIndex: Int = 3
)

enum class Networks(val type: Int, val networkName: String){
    NineMobile(0, "9mobile"),
    Airtel(1, "Airtel"),
    Glo(2, "Globacom"),
    MTN(3, "mtn")

}