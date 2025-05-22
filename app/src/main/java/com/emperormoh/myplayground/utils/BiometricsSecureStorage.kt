package com.emperormoh.myplayground.utils

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BiometricsSecureStorage(private val context: Context) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun storeUserEncryptedCredentials(userId: String, encryptedData: String){
        coroutineScope.launch {
            saveSecureComplexData(context, "${userId}_encrypted_data", encryptedData)
        }
    }

    private suspend fun getUserEncryptedCredentials(userId: String): String? {
        return getSecureComplexData(context, "${userId}_encrypted_data", String::class.java)
    }


    suspend fun isUserEnrolled(userId: String): Boolean {
        val encryptedData = getUserEncryptedCredentials(userId)
       return encryptedData?.isNotEmpty() ?: false
    }
}