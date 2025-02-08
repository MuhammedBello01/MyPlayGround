package com.emperormoh.myplayground.presentation.componenets

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.memory.MemoryCache
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.LightGray
import com.emperormoh.myplayground.ui.theme.WhiteTextColor

@Composable
fun LoadImageFromUrl(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    placeholderMemoryCacheKey: String? = null,
    loadingIndicatorSize: Dp = 20.dp,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderIcon: ImageVector? = null
) {
    val imageLoader = LocalContext.current.imageLoader
    var placeholderBitmap by remember(placeholderMemoryCacheKey) { mutableStateOf<Bitmap?>(null) }
    var isLoading by rememberSaveable(model) { mutableStateOf(true) }

    var hasError by rememberSaveable(model) { mutableStateOf(false) }

    LaunchedEffect(placeholderMemoryCacheKey) {
        placeholderMemoryCacheKey?.let {
            placeholderBitmap =
                imageLoader.memoryCache?.get(MemoryCache.Key(placeholderMemoryCacheKey))?.bitmap
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = {
                isLoading = false
                hasError = false
            },
            onError = {
                isLoading = false
                hasError = true
            },
        )

        AnimatedVisibility(
            visible = isLoading || hasError,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            if (isLoading) {
                // Show CircularProgressIndicator while loading
               CircularProgressIndicator(modifier = Modifier.size(loadingIndicatorSize), color = AlatRed)
            } else if (hasError) {
                if (placeholderBitmap == null && placeholderIcon != null) {
                    // Show the placeholder icon if imageBitmap is null
                    Icon(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize(),
                        imageVector = placeholderIcon,
                        contentDescription = contentDescription,
                    )
                } else if (placeholderBitmap != null) {
                    // Show the cached bitmap if available
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = (placeholderBitmap as Bitmap).asImageBitmap(),
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                    )
                }
            }
        }
    }
}

@Composable
fun LoadImageFromUrlOpt(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    progressIndicatorColor: Color = LightGray,
    loadingIndicatorSize: Dp = 20.dp,
    contentScale: ContentScale = ContentScale.Fit,
    placeholderIcon: @Composable (() -> Unit)? = { Icon(imageVector = Icons.Outlined.Person, contentDescription = "Placeholder") }
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.background(color = WhiteTextColor),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            contentScale = contentScale,
            onSuccess = {
                isLoading = false
                hasError = false
            },
            onError = {
                isLoading = false
                hasError = true
            },
        )
        AnimatedVisibility(
            visible = isLoading || hasError,
            enter = fadeIn(),
            exit = fadeOut(),
        ){
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(loadingIndicatorSize),
                    color = progressIndicatorColor
                )
            } else if (hasError) {
                placeholderIcon?.invoke()
            }
        }
    }
}
