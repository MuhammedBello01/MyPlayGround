package com.emperormoh.myplayground.presentation.componenets.snackBar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SnackbarController(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    fun showSnackbar(
        message: String,
        isError: Boolean = false,
        durationMillis: Long = 3000
    ) {
        coroutineScope.launch {
            val visuals = SnackbarVisualsWithErrorSupport(message, isError)
            val result = snackbarHostState.showSnackbar(visuals)

             //Optional: If user clicks, dismiss immediately
//            if (result == SnackbarResult.ActionPerformed) {
//                snackbarHostState.currentSnackbarData?.dismiss()
//            } else {
//                // Auto-dismiss after timeout
//                delay(durationMillis)
//                snackbarHostState.currentSnackbarData?.dismiss()
//            }
            launch {
                delay(durationMillis)
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        }
    }

    class SnackbarVisualsWithErrorSupport(
        override val message: String,
        val isError: Boolean
    ) : SnackbarVisuals {
        override val actionLabel: String = ""
        override val withDismissAction: Boolean = true
        override val duration: SnackbarDuration = SnackbarDuration.Short
    }
}


data class SnackbarMessage(
    val message: String,
    val isError: Boolean = false
)