package com.emperormoh.myplayground.presentation.componenets.async_component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AsyncPageState(
    pageState: PageState,
    loadingContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    loadedContent: @Composable () -> Unit
) {
    when(pageState){
        PageState.Loading -> {
            loadingContent ?: Box(
                Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        PageState.Loaded -> loadedContent()
        is PageState.Error -> {
            errorContent ?: {}
        }
    }
}