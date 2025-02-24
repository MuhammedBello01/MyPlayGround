package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.runtime.Composable

enum class StepStatus {
    COMPLETED, PENDING, NOT_YET_STARTED
}

data class TransactionStep(
    val status: StepStatus,
    val message: String,
    val timeAndDate: String
)



