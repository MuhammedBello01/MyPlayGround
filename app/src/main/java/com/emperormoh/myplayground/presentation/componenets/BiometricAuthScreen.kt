package com.emperormoh.myplayground.presentation.componenets

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.emperormoh.myplayground.utils.BiometricAuthHandler
import com.emperormoh.myplayground.utils.BiometricAuthUtil
import com.emperormoh.myplayground.utils.FinalBiometricHandler
import kotlinx.coroutines.launch

@Composable
fun BiometricAuthScreen() {
    val context = LocalContext.current
    var isBiometricAvailable by remember { mutableStateOf(false) }
    var authStatus by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val authUtil = BiometricAuthUtil(
            context = context,
            onSuccess = {cryptoObject ->
                // Here you can access the cryptoObject
                val cipher = cryptoObject?.cipher
                authStatus = "Authentication succeeded!"
                // Navigate to the authenticated screen
                //Toast.makeText(context,"$cipher", Toast.LENGTH_SHORT).show()
                Log.e("The crypto cipher", "BiometricAuthScreen: $cipher" )
            },
            onError = { errorCode, errString ->
                authStatus = "Authentication error: $errString (Code: $errorCode)"
            },
            onFailed = {
                authStatus = "Authentication failed. Try again."
            }
        )

        isBiometricAvailable = authUtil.checkBiometricAvailable()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isBiometricAvailable) {
            Button(
                onClick = {
                    val authUtil = BiometricAuthUtil(
                        context = context,
                        onSuccess = {cryptoObject ->
                            // Here you can access the cryptoObject
                            val cipher = cryptoObject?.cipher
                            authStatus = "Authentication succeeded!"
                            Log.e("The crypto cipher", "BiometricAuthScreen: $cipher" )
                        },
                        onError = { errorCode, errString ->
                            authStatus = "Authentication error: $errString (Code: $errorCode)"
                        },
                        onFailed = {
                            authStatus = "Authentication failed. Try again."
                        }
                    )
                    authUtil.showBiometricPrompt(
                        title = "Confirm Using Your Fingerprint",
                        subtitle = "You can use your fingerprint to confirm making payments through this app.",
                        description = "Touch the fingerprint sensor"
                    )
                }
            ) {
                Text("Authenticate with Biometrics")
            }
        } else {
            Text("Biometric authentication is not available on this device")//javax.crypto.Cipher@261847e
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(authStatus)
    }
}

@Composable
fun TestBioFun(){
    val context = LocalContext.current
    val biometricAuthHandler = remember { BiometricAuthHandler(context) }
    var isAuthenticated by remember { mutableStateOf(false) }
    if (isAuthenticated) {
        AuthenticatedContent()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            // Only show biometric button if available
            if (biometricAuthHandler.canAuthenticate()) {
                BiometricAuthButton(
                    biometricAuthHandler = biometricAuthHandler,
                    onAuthSuccess = {
                        isAuthenticated = true
                        Toast.makeText(
                            context,
                            "Biometric authentication successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onAuthError = { errorMessage ->
                        Toast.makeText(
                            context,
                            "Authentication error: $errorMessage",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onAuthFailed = {
                        Toast.makeText(
                            context,
                            "Authentication failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}

/**
 * A Composable button that triggers biometric authentication when clicked
 */
@Composable
fun BiometricAuthButton(
    biometricAuthHandler: BiometricAuthHandler,
    onAuthSuccess: () -> Unit,
    onAuthError: (String) -> Unit,
    onAuthFailed: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            if (biometricAuthHandler.canAuthenticate()) {
                scope.launch {
                    biometricAuthHandler.authenticate(
                        title = "Biometric Authentication",
                        subtitle = "Log in using your biometric credential",
                        description = "Confirm your identity to access the app",
                        negativeButtonText = "Cancel",
                        onSuccess = onAuthSuccess,
                        onError = { _, message -> onAuthError(message) },
                        onFailed = onAuthFailed
                    )
                }
            } else {
                onAuthError("Biometric authentication not available")
            }
        }
    ) {
        Text("Authenticate with Biometrics")
    }
}

@Composable
fun AuthenticatedContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome! You are authenticated.",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun FinalBiometricAuthScreen(
    onAuthSuccess: () -> Unit,
    onAuthError: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val biometricAuthHandler = remember { FinalBiometricHandler(context) }

    var showAuth by remember { mutableStateOf(false) }
    var authError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showAuth = true }) {
            Text("Authenticate with Biometrics")
        }

        authError?.let {
            Spacer(Modifier.height(8.dp))
            Text("Auth Error: $it", color = Color.Red)
        }
    }

    if (showAuth && activity != null) {
        LaunchedEffect(Unit) {
            if (biometricAuthHandler.canAuthenticate()) {
                biometricAuthHandler.authenticate(
                    onSuccess = { cryptoObject ->
                        val cipher = cryptoObject?.cipher
                        Log.e("The crypto cipher", "BiometricAuthScreen: $cipher" )
                        showAuth = false
                        onAuthSuccess()
                    },
                    onError = { _, errorMessage ->
                        showAuth = false
                        authError = errorMessage
                        onAuthError(errorMessage)
                    },
                    onFailed = {
                        showAuth = false
                        authError = "Authentication failed. Try again."
                    },
                )
            } else {
                authError = "Biometric authentication is not available."
                showAuth = false
            }
        }
    }
}
