package com.emperormoh.myplayground.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Base64

val Context.dataStore by preferencesDataStore(name = "secure_prefs")

suspend fun saveSecureData(context: Context, key: String, value: String) {
    val (encryptedValue, iv) = EncryptionHelper.encrypt(value)
    val encryptedString = Base64.getEncoder().encodeToString(encryptedValue)
    val ivString = Base64.getEncoder().encodeToString(iv)

    context.dataStore.edit { preferences ->
        preferences[stringPreferencesKey(key)] = "$encryptedString:$ivString"
    }
}

fun getSecureData(context: Context, key: String): Flow<String?> {
    return context.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]?.let { encryptedData ->
            val (data, iv) = encryptedData.split(":").map { Base64.getDecoder().decode(it) }
            EncryptionHelper.decrypt(data, iv)
        }
    }
}