package com.emperormoh.myplayground.presentation.componenets

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.compositeOver
import kotlin.math.abs


@Composable
fun ReuseAbleSwipeActionsList(
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
fun <T> ReuseAbleSwipeAbleList(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
    onItemDelete: (T) -> Unit,
    onItemEdit: (T) -> Unit,
    editBackgroundColor: Color = colorResource(R.color.CoreUiAlwaysDarkGreen),
    deleteBackgroundColor: Color = colorResource(R.color.CoreUiAlwaysBrightRed),
    editIcon: String? = "edit_beneficiary_animation.json",
    deleteIcon: String? = "delete_beneficiary_animation.json",
    editLabel: String = "Edit",
    deleteLabel: String = "Delete",
    itemSpacing: Dp = 8.dp,
    cornerRadius: Dp = 8.dp,
    swipeThreshold: Float = 0.4f,
    key: ((T) -> Any)? = null  // Optional key for item stability
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        items(
            items = items,
            key = key
        ) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                positionalThreshold = { it * swipeThreshold },
                confirmValueChange = { value ->
                    when (value) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            onItemEdit(item)  // Pass the item directly
                            false
                        }
                        SwipeToDismissBoxValue.EndToStart -> {
                            onItemDelete(item)  // Pass the item directly
                            false
                        }
                        SwipeToDismissBoxValue.Settled -> false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val direction = dismissState.dismissDirection

                    //
                    val progress = abs(dismissState.progress)

                    val color = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> editBackgroundColor
                        SwipeToDismissBoxValue.EndToStart -> deleteBackgroundColor
                        SwipeToDismissBoxValue.Settled -> Color.Transparent
                    }

                    //
                    val animatedColor = if (direction != SwipeToDismissBoxValue.Settled) {
                        color.darkenByProgress(progress)
                    } else {
                        color
                    }


                    val alignment = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                        SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                        SwipeToDismissBoxValue.Settled -> Alignment.Center
                    }

                    val icon = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> editIcon
                        SwipeToDismissBoxValue.EndToStart -> deleteIcon
                        SwipeToDismissBoxValue.Settled -> null
                    }

                    val label = when (direction) {
                        SwipeToDismissBoxValue.StartToEnd -> editLabel
                        SwipeToDismissBoxValue.EndToStart -> deleteLabel
                        SwipeToDismissBoxValue.Settled -> ""
                    }

//                    Card(
//                        modifier = Modifier.fillMaxSize(),
//                        colors = CardDefaults.cardColors(containerColor = color),
//                        shape = RoundedCornerShape(cornerRadius)
//                    ) {
//
//                    }
                    Box(
                        modifier = Modifier.fillMaxSize() .background(
                            color = animatedColor,
                            shape = RoundedCornerShape(cornerRadius)
                        ),
                        contentAlignment = alignment
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        ) {
                            icon?.let {
                                LottieLoaderFromAssets(
                                    assetFileName = it,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.height(5.dp))
                            if (label.isNotEmpty()) {
                                Text(label, color = Color.White)
                            }
                        }
                    }
                },
                content = {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(cornerRadius)
                    ) {
                        itemContent(item)
                    }
                }
            )
        }
    }
}

// Adjustable color darkening with fine-tuned parameters
fun Color.darkenByProgress(progress: Float): Color {
    // Parameters you can adjust:
    val baseDarkness = 0.1f  // Base darkness amount (0f-1f)
    val progressMultiplier = 0.5f  // How much swipe progress affects darkness
    val maxDarkness = 0.8f  // Maximum allowed darkness (0f-1f)

    // Calculate final darkness (capped at maxDarkness)
    val effectiveDarkness = (baseDarkness + (progress * progressMultiplier))
        .coerceIn(0f, maxDarkness)

    return copy(
    red = (red * (1f - effectiveDarkness)).coerceIn(0f, 1f),
    green = (green * (1f - effectiveDarkness)).coerceIn(0f, 1f),
    blue = (blue * (1f - effectiveDarkness)).coerceIn(0f, 1f)
).compositeOver(Color.Black.copy(alpha = progress * 0.15f))
}

data class Person(val id: String, val name: String, val age: Int)

@Composable
fun PersonList() {
    val context = LocalContext.current
    val people = remember { generateMockPeople(5) }
    val peopleState by remember { mutableStateOf(people) }
    ReuseAbleSwipeAbleList(
        items = peopleState,
        key = { it.id },  // Use unique ID for stability
        onItemDelete = { person ->
            // Handle delete with full person object
            Toast.makeText(context, "Delete ${person.name}", Toast.LENGTH_SHORT).show()
        },
        onItemEdit = { person ->
            // Handle edit with full person object
            Toast.makeText(context, "Edit ${person.name}", Toast.LENGTH_SHORT).show()
        },
        itemContent = { person ->
            Column(Modifier.padding(16.dp)) {
                Column(Modifier.padding(10.dp)) {
                    Text(text = person.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = "ID: ${person.id}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Age: ${person.age}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    )
}

fun generateMockPeople(count: Int = 10): List<Person> {
    val firstNames = listOf("John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava", "Benjamin", "Isabella")
    val lastNames = listOf("Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson")

    return List(count) { index ->
        Person(
            id = "P${1000 + index}",
            name = "${firstNames.random()} ${lastNames.random()}",
            age = (18..65).random()
        )
    }
}
