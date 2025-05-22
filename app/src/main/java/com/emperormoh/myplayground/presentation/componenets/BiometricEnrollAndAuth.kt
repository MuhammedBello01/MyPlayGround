package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.runtime.Composable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.utils.BiometricAuthHandler

@Composable
fun BiometricEnrollAndAuth(){

    val context = LocalContext.current
    val biometricAuthHandler = remember { BiometricAuthHandler(context) }
    var isAuthenticated by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var authStatus by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(onClick = {
            if (!biometricAuthHandler.canAuthenticate()){
                authStatus = "Biometric authentication not available on this device"
            }else{
                biometricAuthHandler.enrollUserBiometric(
                    userId = "BabaMoh",
                    onSuccess = {},
                    onError = {err -> authStatus = err}
                )
            }

        }) { Text(text = "Enroll Biometrics") }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {

        }) { Text(text = "Authenticate Biometrics") }

        Spacer(modifier = Modifier.height(20.dp))

        Text(authStatus)
    }
}