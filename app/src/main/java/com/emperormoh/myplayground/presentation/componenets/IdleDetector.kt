package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

@Composable
fun IdleDetector(
    idleTimeoutMillis: Long = 20_000,
    onIdle: () -> Unit,
    content: @Composable () -> Unit
) {
    var lastInteractionTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    // Listen for pointer events anywhere inside the content
    val interactionModifier = Modifier.pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                awaitPointerEvent()
                lastInteractionTime = System.currentTimeMillis()
            }
        }
    }

    LaunchedEffect(lastInteractionTime) {
        delay(idleTimeoutMillis)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInteractionTime >= idleTimeoutMillis) {
            onIdle()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(interactionModifier)
    ) {
        content()
    }
}
