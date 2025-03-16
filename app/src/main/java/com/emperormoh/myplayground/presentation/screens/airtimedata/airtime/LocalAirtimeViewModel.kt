package com.emperormoh.myplayground.presentation.screens.airtimedata.airtime

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.emperormoh.myplayground.ONNXModelLoader
import com.emperormoh.myplayground.presentation.screens.common.MobileNetworks
import com.emperormoh.myplayground.presentation.screens.common.convertNetworkIndexToName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalAirtimeViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(LocalAirtimeUiState())
    val uiState: StateFlow<LocalAirtimeUiState> = _uiState.asStateFlow()

    private var modelLoader: ONNXModelLoader? = null

    fun predictPhoneNetwork(phoneNumber: String): MobileNetworks {
        try {
            val inputArray = preprocessPhoneNumber(phoneNumber) // Scaled 11-digit input
            val (predictedClass, confidence) = modelLoader?.predict(inputArray)!!
            println("Predicted Class: $predictedClass, Confidence: $confidence")
            _uiState.update {
                it.copy(
                    phoneNumber = phoneNumber,
                    isPhoneNumberPredictionLoading = false,
                    isPhoneNumberPredicted = true,
                    predictedNetworkIndex = predictedClass,
                    selectedNetwork = convertNetworkIndexToName(predictedClass)
                )
            }
            return when(predictedClass){
                MobileNetworks.NineMobile.type -> MobileNetworks.NineMobile
                MobileNetworks.Airtel.type -> MobileNetworks.Airtel
                MobileNetworks.Glo.type -> MobileNetworks.Glo
                MobileNetworks.MTN.type -> MobileNetworks.MTN
                else -> MobileNetworks.MTN
            }
        }catch (e: Exception){
            _uiState.update {
                it.copy(
                    isPhoneNumberPredictionLoading = false,
                    isPhoneNumberPredicted = true,
                    predictedNetworkIndex =  MobileNetworks.MTN.type,
                    selectedNetwork = convertNetworkIndexToName(MobileNetworks.MTN.type)
                )
            }
            Log.e("PredictNetwork", "Error during network prediction: ${e.message}", e)
            return MobileNetworks.MTN
        }
    }

    // Method to Convert Full 11-Digit Phone Number to Input Array
    private fun preprocessPhoneNumber(phoneNumber: String): FloatArray {
        return phoneNumber.map { it.toString().toFloat() / 9.0f }.toFloatArray()
    }

    fun initModel(context: Context){
         modelLoader = ONNXModelLoader(context)
    }

    fun onPhoneNumberChanged(phoneNumber: String){
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber,
                isPhoneNumberPredictionLoading = false,
                isPhoneNumberPredicted = false,
                //predictedNetworkIndex =  Networks.MTN.type
            )
        }
    }
    fun onNetworkSelectionChanged(index: Int){
        _uiState.update {
            it.copy(
                isPhoneNumberPredictionLoading = false,
                isPhoneNumberPredicted = true,
                predictedNetworkIndex =  index,
                selectedNetwork = convertNetworkIndexToName(index)
            )
        }
    }


}

data class LocalAirtimeUiState(
    val phoneNumber: String = "",
    val isPhoneNumberPredicted: Boolean = false,
    val isPhoneNumberPredictionLoading: Boolean = false,
    var selectedNetwork: String = "",
    val predictedNetworkIndex: Int = 3,
)

