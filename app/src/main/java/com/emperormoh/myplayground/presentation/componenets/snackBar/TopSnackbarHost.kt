package com.emperormoh.myplayground.presentation.componenets.snackBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@Composable
fun TopSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { snackbarData ->
                val isError = (snackbarData.visuals as? SnackbarController.SnackbarVisualsWithErrorSupport)?.isError
                    ?: false

                val backgroundColor = if (isError) Color.Red else Color(0xFF4CAF50)

                Snackbar(
                    modifier = Modifier
                        .clickable { snackbarData.dismiss() },
                    snackbarData = snackbarData,
                    containerColor = backgroundColor,
                    contentColor = Color.White
                )
            }
        )
    }
}

@Composable
fun AnimatedTopSnackbarHost(snackbarHostState: SnackbarHostState) {
    val currentSnackbarData = snackbarHostState.currentSnackbarData

    AnimatedVisibility(
        visible = currentSnackbarData != null,
        enter = slideInVertically(initialOffsetY = { -100 }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -100 }) + fadeOut(),
    ) {
        currentSnackbarData?.let { data ->
            val visuals = data.visuals as? SnackbarController.SnackbarVisualsWithErrorSupport
            val isError = visuals?.isError ?: false
            val backgroundColor = if (isError) Color.Red else Color(0xFF4CAF50)
            val icon = if (isError) Icons.Default.Error else Icons.Default.CheckCircle

            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                containerColor = backgroundColor,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left section: Icon + Message
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(data.visuals.message)
                    }

                    // Right section: Close icon
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { data.dismiss() }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSnackBarTestScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarController = remember { SnackbarController(snackbarHostState, coroutineScope) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = { TopAppBar(title = { Text("My App") }) },
            snackbarHost = { AnimatedTopSnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    snackbarController.showSnackbar("Something went wrong!", isError = true)
                }) {
                    Text("Trigger Error Snackbar")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    snackbarController.showSnackbar("Success! Operation completed.", isError = false)
                }) {
                    Text("Trigger Success Snackbar")
                }
            }
        }
    }

}

