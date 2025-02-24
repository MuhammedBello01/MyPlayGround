package com.emperormoh.myplayground.presentation.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.utils.getSecureData
import com.emperormoh.myplayground.utils.saveSecureData
import kotlinx.coroutines.launch

@Composable
fun SecureDataScreen(context: Context) {
    val scope = rememberCoroutineScope()
    var retrievedData by remember { mutableStateOf("No data") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            scope.launch { saveSecureData(context, "username", "JohnDoe123990") }
        }) {
            Text("Save Secure Data")
        }

        Button(onClick = {
            scope.launch {
                getSecureData(context, "username").collect { data ->
                    retrievedData = data ?: "No data"
                }
            }
        }) {
            Text("Retrieve Secure Data")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Retrieved Data: $retrievedData")
    }
}
