package com.emperormoh.myplayground.presentation.screens

import android.util.Log
import androidx.annotation.Keep
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.emperormoh.myplayground.presentation.componenets.TransferBeneficiary
import com.emperormoh.myplayground.presentation.componenets.getMockTransferBeneficiaryResponse
import com.emperormoh.myplayground.presentation.componenets.getMockTransferFrequentBeneficiaryResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalTransferViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LocalTransferUiState())
    val uiState: StateFlow<LocalTransferUiState> = _uiState.asStateFlow()

     private val _originalBanks = mutableListOf<Bank>() // Store the full bank list
    private var theBankSelected by mutableStateOf<Bank?>(null)

    init {
        getAllBanks()
    }

    suspend fun onAccountDetailsTypeControl(input: String){
        when(checkStringType(input)){
            InputType.NUMBERS -> predictBank(input)
            InputType.ALPHABETS -> {}
            InputType.MIXED -> {
                _uiState.update {
                    it.copy(
                        isPredictBankLoading = false,
                        predictedBanks = emptyList(),
                        showPredictedBanks = false,
                        invalidParameter = "Mixed characters not allowed"
                    )
                }
            }
        }
    }

    suspend fun predictBank(bankCode: String){
        getAllBanks()
        _uiState.update {
            it.copy(
                isPredictBankLoading = true,
                predictedBanks = emptyList(),
                showPredictedBanks = false,
                transferData = null,
                isAccountNumberVerificationLoading = false,
                isAccountNumberVerified = false,
            )
        }
        try{
            val predictedBanks = getPredictedBanks().filter { it.bankCode.contains(bankCode, ignoreCase = true) }
            delay(2000)
            _uiState.update {
                it.copy(
                    isPredictBankLoading = false,
                    predictedBanks = predictedBanks,
                    showPredictedBanks = true,
                    transferData = null,
                    isAccountNumberVerificationLoading = false,
                    isAccountNumberVerified = false,
                )
            }
        }
        catch (e: Exception) {
            // Handle exceptions related to the ML model
            _uiState.update {
                it.copy(
                    isPredictBankLoading = false,
                    predictedBanks = emptyList(),
                    showPredictedBanks = true,
                    transferData = null,
                    isAccountNumberVerificationLoading = false,
                    isAccountNumberVerified = false,
                )
            }
            // Log the error for debugging
            Log.e("PredictBank", "Error during bank prediction: ${e.message}", e)
        }
    }

    fun checkStringType(input: String): InputType {
        return when {
            input.all { it.isDigit() } -> InputType.NUMBERS
            input.all { it.isLetter() } -> InputType.ALPHABETS
            else -> InputType.MIXED
        }
    }

    fun setSelectedBank(bank: Bank){

        _uiState.update {
            it.copy(
                isPredictBankLoading = false,
                showPredictedBanks = false,
                transferData = TransferData(
                    bank = bank
                ),
                isAccountNumberVerificationLoading = true,
                isAccountNumberVerified = false,
            )
        }
        theBankSelected = bank
    }

    fun resetFieldsUiOnAccountNumberChanged(){
        _uiState.update {
            it.copy(
                isPredictBankLoading = false,
                predictedBanks = emptyList(),
                showPredictedBanks = false,
                transferData = null,
                isAccountNumberVerificationLoading = false,
                isAccountNumberVerified = false,
            )
        }
    }

    fun searchBanks(query: String?) {
       if (query.isNullOrBlank() || _originalBanks.isEmpty()) return

        val filteredBanks = _originalBanks.filter { it.bankName.contains(query, ignoreCase = true) }
        _uiState.update {
            it.copy(
                allBanks = filteredBanks,
                showAllBanks = true
            )
        }
//        _uiState.value = _uiState.value.copy(
//            allBanks = filteredBanks,
//            showAllBanks = true
//        )
    }

    fun setBanks(banks: List<Bank>) {
        _originalBanks.clear()
        _originalBanks.addAll(banks)
        _uiState.update { it.copy(allBanks = banks,
        showAllBanks = true) }
    }

    fun getAllBanks(): List<Bank>{
        _uiState.update {
            it.copy(
                allBanks = allBanksMock(),
                showAllBanks = true
            )
        }
        return allBanksMock()
    }

    suspend fun verifyAccountNumber(accountNumber: String){

        delay(3000)
        _uiState.update {
            it.copy(
                isPredictBankLoading = false,
                showPredictedBanks = false,
                transferData = TransferData(
                   accountNumber = accountNumber,
                    nickname = "Suleiman Muhammed Gladiola",
                    bank = theBankSelected

                ),
                isAccountNumberVerificationLoading = false,
                isAccountNumberVerified = true
            )
        }
    }

    fun getFrequentBeneficiaries(){
        _uiState.update {
            it.copy(
               frequentBeneficiaries = getMockTransferFrequentBeneficiaryResponse().beneficiaries
            )
        }
    }

    fun getSavedBeneficiaries(){
        _uiState.update {
            it.copy(
                savedBeneficiaries = getMockTransferBeneficiaryResponse().beneficiaries
            )
        }
    }
    private fun getPredictedBanks() = listOf(
        Bank(bankCode = "0000000000", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000004.png", bankName = "UNITED BANK FOR AFRICA"),
        Bank(bankCode = "1111111111", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000003.png", bankName = "FCMB"),
        Bank(bankCode = "2222222222", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000011.png", bankName = "UNITY BANK"),
    )

    private fun allBanksMock()  = listOf(
        Bank(bankCode = "090110", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/090110.png", bankName = "VFD MFB"),
        Bank(bankCode = "000015", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000015.png", bankName = "ZENITH BANK"),
        Bank(bankCode = "000018", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000018.png", bankName = "UNION BANK"),
        Bank(bankCode = "000026", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000026.png", bankName = "TAJ BANK"),
        Bank(bankCode = "000012", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000012.png", bankName = "STANBIC IBTC BANK"),
        Bank(bankCode = "000002", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000002.png", bankName = "KEYSTONE BANK"),
        Bank(bankCode = "000006", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000006.png", bankName = "JAIZ BANK"),
        Bank(bankCode = "000007", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000007.png", bankName = "FIDELITY BANK"),
        Bank(bankCode = "090551", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/090551.png", bankName = "FairMoney MFB"),
        Bank(bankCode = "090267", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/090267.png", bankName = "KUDA MICROFINANCE BANK"),
        Bank(bankCode = "000003", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000003.png", bankName = "FCMB"),
        Bank(bankCode = "000004", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000004.png", bankName = "UNITED BANK FOR AFRICA"),
        Bank(bankCode = "000011", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000011.png", bankName = "UNITY BANK"),
        Bank(bankCode = "035", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/035.png", bankName = "ALATbyWEMA"),
        //Bank(bankCode = "035", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/035.png", bankName = "WEMA BANK"),
        Bank(bankCode = "000013", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000013.png", bankName = "GUARANTY TRUST BANK"),
        Bank(bankCode = "000010", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000010.png", bankName = "ECOBANK"),
    )
}

enum class InputType {
    NUMBERS, ALPHABETS, MIXED
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
    val invalidParameter: String = "",
    val showAllBanks: Boolean = false,
    var bankSearchQuery: String = "",
    val isAccountNumberVerificationLoading: Boolean = false,
    val isAccountNumberVerified: Boolean = false,

    val savedBeneficiaries: List<TransferBeneficiary> = emptyList(),
    val frequentBeneficiaries: List<TransferBeneficiary> = emptyList()
)

data class Bank(
    val bankCode: String,
    val bankLogo: String,
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