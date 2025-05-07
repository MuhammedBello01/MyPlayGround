package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.emperormoh.myplayground.R

@Composable
fun SwipeActionsListMaterial3(
    items: List<String>,
    onItemDelete: (Int) -> Unit,
    onItemEdit: (Int) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(items) { index, item ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { it * 0.4f },
                confirmValueChange = { value ->
                    when (value) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            onItemEdit(index)
                            false // Don't dismiss, just perform edit action
                        }
                        SwipeToDismissBoxValue.EndToStart -> {
                            onItemDelete(index)
                            false // Actually remove the item
                        }
                        SwipeToDismissBoxValue.Settled -> false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val direction = dismissState.dismissDirection
                    val color = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF2196F3) // Blue for edit
                        SwipeToDismissBoxValue.EndToStart -> Color(0xFFE53935) // Red for delete
                        SwipeToDismissBoxValue.Settled -> Color.Transparent
                    }

                    val alignment = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        SwipeToDismissBoxValue.Settled -> Alignment.Center
                    }

//                    val icon = when (direction) {
//                        SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
//                        SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
//                        SwipeToDismissBoxValue.Settled -> null
//                    }

                    val icon = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> "edit_beneficiary_animation.json"
                        SwipeToDismissBoxValue.EndToStart -> "delete_beneficiary_animation.json"
                        SwipeToDismissBoxValue.Settled -> null
                    }

                    Card(
                        modifier = Modifier.fillMaxSize(),
                        colors = CardDefaults.cardColors(containerColor = color),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = alignment
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(end = 32.dp, start = 32.dp)
                            ) {
                                icon?.let {
                                    LottieLoaderFromAssets(
                                        assetFileName = icon,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                                Spacer(Modifier.height(5.dp))
                                Text("Delete", color = colorResource(R.color.CoreUiAlwaysWhite))
                            }
//                            icon?.let {
//                                Icon(
//                                    imageVector = it,
//                                    contentDescription = if (direction == SwipeToDismissBoxValue.StartToEnd) "Edit" else "Delete",
//                                    tint = Color.White,
//                                    modifier = Modifier.padding(horizontal = 24.dp)
//                                )
//                            }
                        }
                    }
                },
                content = {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun LottieLoaderFromAssets(
    assetFileName: String,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(assetFileName))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed,
        restartOnPlay = true
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}