package com.emperormoh.myplayground.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.Base64

val Context.dataStore by preferencesDataStore(name = "secure_prefs")

private val gson = GsonBuilder()
    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer)
    .create()

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

suspend fun saveSecureComplexData(context: Context, key: String, value: Any){
    val (encryptedValue, iv) = EncryptionHelper.encrypt(gson.toJson(value))
    val encryptedString = Base64.getEncoder().encodeToString(encryptedValue)
    val ivString = Base64.getEncoder().encodeToString(iv)

    context.dataStore.edit { preferences ->
        preferences[stringPreferencesKey(key)] = "$encryptedString:$ivString"
    }
}

suspend fun <T> getSecureComplexData(context: Context, key: String, type: Class<T>): T? {
    val storedData = context.dataStore.data.first()[stringPreferencesKey(key)] ?: return null

    val (encryptedString, ivString) = storedData.split(":")
    val encryptedBytes = Base64.getDecoder().decode(encryptedString)
    val ivBytes = Base64.getDecoder().decode(ivString)

    val decryptedJson = EncryptionHelper.decrypt(encryptedBytes, ivBytes)  // 🔓 Decrypt JSON
    return gson.fromJson(decryptedJson, type)  // 📜 Convert back to object
}