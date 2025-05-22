package com.emperormoh.myplayground.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * A utility class for handling biometric authentication in a Jetpack Compose app
 */
class BiometricAuthHandler(private val context: Context) {

    private val biometricManager = BiometricManager.from(context)
    private val secureStorage = BiometricsSecureStorage(context)

    fun canAuthenticate(): Boolean {
        return when (biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("BiometricAuthHandler", "No biometric hardware")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("BiometricAuthHandler", "Biometric hardware unavailable")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("BiometricAuthHandler", "No biometric enrolled")
                false
            }
            else -> false
        }
    }

    fun authenticate(
        title: String,
        subtitle: String,
        description: String,
        negativeButtonText: String,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errorCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG)
            .build()

        val biometricPrompt = BiometricPrompt( context as FragmentActivity, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    }

    /**
     * Checks if a user has already enrolled their biometric
     */
    suspend fun isUserEnrolled(userId: String): Boolean {
        return secureStorage.isUserEnrolled(userId)
    }

    private fun createBiometricSecuredKey(keyName: String) {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val keyProperties = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(keyName, keyProperties)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(true) // Requires biometric auth
            .setUserAuthenticationValidityDurationSeconds(15)
            .build()

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getCipherForEncryption(keyName: String): Cipher? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias(keyName)) {
            return null
        }

        val key = keyStore.getKey(keyName, null) as SecretKey
        val cipher = Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
        )
        cipher.init(Cipher.ENCRYPT_MODE, key)

        return cipher
    }

    private fun getCipherForDecryption(keyName: String): Cipher? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (!keyStore.containsAlias(keyName)) {
            return null
        }

        val key = keyStore.getKey(keyName, null) as SecretKey
        val cipher = Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
        )

        // For a real authentication, we'd retrieve the stored IV
        // For simplicity, we're using encrypt mode just to verify biometric
        cipher.init(Cipher.ENCRYPT_MODE, key)

        return cipher
    }


    private fun showBiometricPromptForEnrollment(
        userId: String,
        title: String,
        subtitle: String,
        description: String,
        negativeButtonText: String,
        cipher: Cipher,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                try {
                    // Encrypt some test data to verify enrollment worked
                    val dataToEncrypt = "Biometric verification data for $userId".toByteArray(Charsets.UTF_8)
                    val encryptedData = result.cryptoObject?.cipher?.doFinal(dataToEncrypt)
                    val iv = result.cryptoObject?.cipher?.iv

                    if (encryptedData != null && iv != null) {
                        // Store encrypted data and IV for future verification
                        val encryptedBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT)
                        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)

                        secureStorage.storeUserEncryptedCredentials(userId, encryptedBase64)
                        onSuccess()
                    } else {
                        onError("Encryption failed")
                    }
                } catch (e: Exception) {
                    Log.e("BiometricAuthHandler", "Enrollment encryption error", e)
                    onError("Encryption error: ${e.message}")
                }
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError("Authentication error: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // During enrollment, this isn't typically critical
                Log.d("BiometricAuthHandler", "Authentication attempt failed")
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG)
            .build()

        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, callback)
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    private fun showBiometricPromptForAuth(
        userId: String,
        title: String,
        subtitle: String,
        description: String,
        negativeButtonText: String,
        cipher: Cipher,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError("Authentication error: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricAuthHandler", "Authentication attempt failed")
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText(negativeButtonText)
            .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG)
            .build()

        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, callback)
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    fun authenticateUser(
        userId: String,
        title: String = "Authentication Required",
        subtitle: String = "Confirm your identity",
        description: String ="Use your biometric to verify your identity",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,

    ) {
        try {
            val keyName = "biometric_key_$userId"
            val cipher = getCipherForDecryption(keyName)

            if (cipher != null) {
                showBiometricPromptForAuth(
                    userId = userId,
                    cipher = cipher,
                    onSuccess = onSuccess,
                    onError = onError,
                    title = title,
                    subtitle = subtitle,
                    description = description,
                    negativeButtonText = negativeButtonText
                )
            } else {
                onError("User not enrolled or key unavailable")
            }
        } catch (e: Exception) {
            Log.e("BiometricAuthHandler", "Authentication error", e)
            onError(e.message ?: "Unknown error during authentication")
        }
    }

    /**
     * Enrolls a user's biometric by creating a secure key linked to their biometric
     */
    fun enrollUserBiometric(
        userId: String,
        title: String = "Enroll Biometric",
        subtitle: String = "Register your biometric for secure access",
        description: String = "Place your finger on the sensor or look at the camera",
        negativeButtonText: String = "Cancel",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Create key for this user
            val keyName = "biometric_key_$userId"
            createBiometricSecuredKey(keyName)

            // Get cipher for encryption
            val cipher = getCipherForEncryption(keyName)
            if (cipher != null) {
                showBiometricPromptForEnrollment(
                    userId = userId,
                    cipher = cipher,
                    onSuccess = onSuccess,
                    onError = onError,
                    title = title,
                    subtitle = subtitle,
                    description = description,
                    negativeButtonText = negativeButtonText
                )
            } else {
                onError("Failed to initialize encryption")
            }
        } catch (e: Exception) {
            Log.e("BiometricAuthHandler", "Enrollment error", e)
            onError(e.message ?: "Unknown error during enrollment")
        }
    }
}




