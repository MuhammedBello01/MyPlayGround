package com.emperormoh.myplayground.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
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

class FinalBiometricHandler(private val context: Context) {

    private val biometricManager = BiometricManager.from(context)
    private val keyName = "biometric_key_name" // Unique key name for the app
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

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
        title: String = "Confirm Using Your Fingerprint",
        subtitle: String = "You can use your fingerprint to confirm making payments through this app.",
        description: String = "Touch the fingerprint sensor",
        negativeButtonText: String = "Cancel",
        onSuccess: (cryptoObject: BiometricPrompt.CryptoObject?) -> Unit,
        onError: (errorCode: Int, errorMessage: String) -> Unit,
        onFailed: () -> Unit
    ) {
        if (context !is FragmentActivity) {
            onError(-1, "Context must be a FragmentActivity")
            return
        }
        val executor = ContextCompat.getMainExecutor(context)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess(result.cryptoObject)
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

        try {
            val cipher = getCipher()
            val secretKey = getSecretKey()
            try {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            } catch (e: KeyPermanentlyInvalidatedException) {
                // Handle key invalidation (e.g., fingerprint change)
                keyStore.deleteEntry(keyName)
                val newKey = getSecretKey()
                cipher.init(Cipher.ENCRYPT_MODE, newKey)
            }

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText(negativeButtonText)
                .setAllowedAuthenticators(Authenticators.BIOMETRIC_STRONG)
                .build()

            val biometricPrompt = BiometricPrompt(context, executor, callback)
            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        } catch (e: Exception) {
            onError(-1, "Failed to initialize biometric authentication: ${e.message}")
        }
    }

    private fun getSecretKey(): SecretKey {
        // Try to get existing key
        val existingKey = keyStore.getKey(keyName, null) as? SecretKey
        if (existingKey != null) {
            return existingKey
        }

        // Create new key if doesn't exist
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyName,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        )
    }
}