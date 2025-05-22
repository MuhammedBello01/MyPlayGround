package com.emperormoh.myplayground.utils

import android.content.Context
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import android.security.keystore.KeyGenParameterSpec
import javax.crypto.KeyGenerator

class BiometricAuthUtil(
    private val context: Context,
    private val onSuccess: (cryptoObject: BiometricPrompt.CryptoObject?) -> Unit,
    private val onError: (errorCode: Int, errString: CharSequence) -> Unit,
    private val onFailed: () -> Unit
) {
    private lateinit var biometricPrompt: BiometricPrompt

    private val keyName = "biometric_key_name" // Unique key name for your app
    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    fun checkBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
    }

    fun showBiometricPrompt(title: String, subtitle: String, description: String) {
        val executor = ContextCompat.getMainExecutor(context)

        biometricPrompt = BiometricPrompt(
            context as FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result.cryptoObject)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )


        try {
            val cipher = getCipher()
            val secretKey = getSecretKey()
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText("Cancel")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .build()

            biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
        } catch (e: Exception) {
            onError(-1, "Failed to initialize biometric authentication: ${e.message}")
        }

//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle(title)
//            .setSubtitle(subtitle)
//            .setNegativeButtonText("Cancel")
//            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
//            .build()
//
//        biometricPrompt.authenticate(promptInfo)
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

    //BiometricPrompt.CryptoObject()
}