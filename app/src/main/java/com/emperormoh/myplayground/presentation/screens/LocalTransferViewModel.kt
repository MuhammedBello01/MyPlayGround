package com.emperormoh.myplayground.presentation.screens

import android.util.Log
import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalTransferViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LocalTransferUiState())
    val uiState: StateFlow<LocalTransferUiState> = _uiState.asStateFlow()

    suspend fun predictBank(bankName: String){
        _uiState.update {
            it.copy(
                isPredictBankLoading = true,
                predictedBanks = emptyList(),
                showPredictedBanks = false
            )
        }

        try{

            val aiBanks = listOf(
                Bank(bankCode = "001", bankLogo = "https://example.com/logo1.png", bankName = "First Bank"),
                Bank(bankCode = "002", bankLogo = "https://example.com/logo2.png", bankName = "Second Bank"),
                Bank(bankCode = "003", bankLogo = "https://example.com/logo3.png", bankName = "Third Bank"),
                Bank(bankCode = "004", bankLogo = null, bankName = "Fourth Bank"),
                Bank(bankCode = "005", bankLogo = "https://example.com/logo5.png", bankName = "Fifth Bank"),
                Bank(bankCode = "006", bankLogo = "https://example.com/logo1.png", bankName = "First Bank"),
                Bank(bankCode = "007", bankLogo = "https://example.com/logo2.png", bankName = "Second Bank"),
                Bank(bankCode = "008", bankLogo = "https://example.com/logo3.png", bankName = "Third Bank"),
                Bank(bankCode = "009", bankLogo = null, bankName = "Fourth Bank"),
                Bank(bankCode = "0010", bankLogo = "https://example.com/logo5.png", bankName = "Fifth Bank")

            )

            val predictedBanks = aiBanks.filter { it.bankName.contains(bankName, ignoreCase = true) }
            delay(2000)

            _uiState.update {
                it.copy(
                    isPredictBankLoading = false,
                    predictedBanks = predictedBanks,
                    showPredictedBanks = true
                )
            }
        }
        catch (e: Exception) {
            // Handle exceptions related to the ML model
            _uiState.update {
                it.copy(
                    isPredictBankLoading = false,
                    predictedBanks = emptyList(),
                    showPredictedBanks = true
                )
            }
            // Log the error for debugging
            Log.e("PredictBank", "Error during bank prediction: ${e.message}", e)
        }
    }

    fun dismissPredictionColumn(){
        _uiState.update {
            it.copy(
                isPredictBankLoading = false,
                showPredictedBanks = false
            )
        }
    }

}

data class LocalTransferUiState(
    val walletBalance: Double = 0.0,
    var destinationAccountNumber: String = "",
    val recentTransactionsSearchQuery: String = "",
    val beneficiariesSearchQuery: String = "",
    val destinationBank: Bank? = null,
    val isBalanceVisible: Boolean = false,
    val shouldNavigate: Boolean = false,
    val predictedBanks: List<Bank> = emptyList(),
    val allBanks: List<Bank> = emptyList(),
    val isPredictBankLoading: Boolean = false,
    val showPredictedBanks: Boolean = false,
    val transferData: TransferData? = null,
    val recentTransactions: List<TransferData> = emptyList(),
    val beneficiaries: List<TransferData> = emptyList(),
)

data class Bank(
    val bankCode: String,
    val bankLogo: String? = null,
    val bankName: String
)

data class TransferData(
    val name: String = "",
    val accountNumber: String? = null,
    val nickname: String? = null,
    val bank: Bank? = null,
    val amount: Double = 0.0,
    val reason: String = "",
    val charges: List<TransferCharge> = defaultCharges(),
    val isSaveBeneficiary: Boolean = false
)
@Keep
data class TransferCharge(
    val charge: Double,
    val chargeFeeName: String?,
    val id: Int?,
    val lower: Double,
    val transactionType: Int?,
    val upper: Double
)

fun defaultCharges() = listOf(
    TransferCharge(
        id = 1,
        chargeFeeName = "InterBank1",
        transactionType = 1,
        charge = 10.75,
        lower = 0.0,
        upper = 5000.0
    ),
    TransferCharge(
        id = 2,
        chargeFeeName = "Interbank2",
        transactionType = 1,
        charge = 26.88,
        lower = 5001.0,
        upper = 50000.0
    ),
    TransferCharge(
        id = 3,
        chargeFeeName = "Interbank3",
        transactionType = 1,
        charge = 53.75,
        lower = 50001.0,
        upper = 0.0
    )
)