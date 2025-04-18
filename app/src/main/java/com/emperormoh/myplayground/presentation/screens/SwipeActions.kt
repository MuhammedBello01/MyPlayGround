package com.emperormoh.myplayground.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.presentation.componenets.SwipeActionsListMaterial3


@Composable
fun SwipeActionsList(
    items: List<String>,
    onItemDelete: (Int) -> Unit,
    onItemEdit: (Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(items) { index, item ->
            SwipeActionsItem(
                item = item,
                onDelete = { onItemDelete(index) },
                onEdit = { onItemEdit(index) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeActionsItem(
    item: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    onEdit()
                    false // Don't dismiss, just trigger edit action
                }
                DismissValue.DismissedToStart -> {
                    onDelete()
                    false // Actually remove the item
                }
                else -> false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.3f) },
        background = {
            val direction = dismissState.dismissDirection

            val color = when (direction) {
                DismissDirection.StartToEnd -> Color(0xFF2196F3) // Blue for edit
                DismissDirection.EndToStart -> Color(0xFFE53935) // Red for delete
                null -> Color.Transparent
            }

            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
                null -> Alignment.Center
            }

            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Star
                DismissDirection.EndToStart -> Icons.Default.Delete
                null -> Icons.Default.Delete
            }

            val iconTint = Color.White

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (direction == DismissDirection.StartToEnd) "Edit" else "Delete",
                    tint = iconTint
                )
            }
        },
        dismissContent = {
            Card(
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
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

@Composable
fun MainScreen() {
    val items = remember { mutableStateListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") }

    SwipeActionsListMaterial3(
        items = items,
        onItemDelete = { index ->
            items.removeAt(index)
        },
        onItemEdit = { index ->
            // Handle edit action here, e.g., navigate to edit screen or show dialog
            Log.d("SwipeAction", "Edit item at position $index: ${items[index]}")
        }
    )
}
