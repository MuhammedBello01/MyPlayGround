package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AppSnackBarHost(
    snackBarData: SnackBarData,
    modifier: Modifier = Modifier,
    autoDismissDuration: Long = 3000L, // default: 3 seconds
    onDismiss: () -> Unit
) {
    AnimatedVisibility(visible = snackBarData.isVisible, modifier = modifier) {
        val backgroundColor = when (snackBarData.type) {
            SnackBarType.SUCCESS -> Color(0xFFDFF0D8) // Light green
            SnackBarType.ERROR -> Color(0xFFF8D7DA) // Light red
        }

        val textColor = when (snackBarData.type) {
            SnackBarType.SUCCESS -> Color(0xFF2E7D32)
            SnackBarType.ERROR -> Color(0xFFD32F2F)
        }

        val icon = when (snackBarData.type) {
            SnackBarType.SUCCESS -> Icons.Default.CheckCircle
            SnackBarType.ERROR -> Icons.Default.Error
        }

        val iconColor = when (snackBarData.type) {
            SnackBarType.SUCCESS -> Color(0xFF2E7D32)
            SnackBarType.ERROR -> Color(0xFFD32F2F)
        }

        // Auto-dismiss logic inside the component
        LaunchedEffect(snackBarData.message) {
            if (snackBarData.isVisible) {
                delay(autoDismissDuration)
                onDismiss()
            }
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = backgroundColor,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = icon, contentDescription = null, tint = iconColor)

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = snackBarData.message,
                    color = textColor,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = textColor,
                    modifier = Modifier
                        .clickable { onDismiss() }
                        .padding(start = 8.dp)
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SnackBarPreview() {
    var snackBarData by remember {
        mutableStateOf(
            SnackBarData(
                message = "This is a success message!",
                type = SnackBarType.SUCCESS,
                isVisible = true
            )
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(color = Color.White)){
        AppSnackBarHost(
            snackBarData = snackBarData,
            onDismiss = { snackBarData = snackBarData.copy(isVisible = false) },
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        )
    }
}


enum class SnackBarType {
    SUCCESS,
    ERROR
}

data class SnackBarData(
    val message: String,
    val type: SnackBarType,
    val isVisible: Boolean = false
)
//var snackBarState by remember { mutableStateOf(SnackBarData(message = "Action successful!", type = SnackBarType.SUCCESS, isVisible = false)) }
//var snackBarData by remember { mutableStateOf(SnackBarData("", SnackBarType.SUCCESS, false)) }



//            when (dataPlanResultState) {
//                is DataPlanResultState.Success -> {
//                    snackBarData = snackBarData.copy(message = "SuccessFul", type = SnackBarType.SUCCESS, isVisible = true)
//                    //snackBarState = snackBarState.copy(message =  "SuccessFul", type = SnackBarType.SUCCESS, isVisible = true)
//
//                }
//                is DataPlanResultState.Failure -> {
//                    //snackBarState = snackBarState.copy(message = dataPlanResultState.errorMessage, type = SnackBarType.ERROR, isVisible = true)
//                    snackBarData = snackBarData.copy(message = dataPlanResultState.errorMessage, type = SnackBarType.ERROR, isVisible = true)
//                }
//                DataPlanResultState.Loading -> { AlatProgressBar() }
//                DataPlanResultState.Default -> {}
//            }
//            AppSnackBarHost(
//                snackBarData = snackBarData,
//                onDismiss = {
//                    snackBarData = snackBarData.copy(message = "", type = SnackBarType.ERROR, isVisible = false)
//                    Log.e("TAG", "LocalDataAccountScreen: Snackbar clicked...............", )
//                },
//                modifier = Modifier
//                    .align(Alignment.TopCenter)
//                    .padding(horizontal = 16.dp)
//            )
