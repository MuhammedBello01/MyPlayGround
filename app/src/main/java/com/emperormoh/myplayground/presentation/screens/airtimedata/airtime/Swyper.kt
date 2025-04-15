package com.emperormoh.myplayground.presentation.screens.airtimedata.airtime

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun BeneficiaryListWise(
    beneficiaries: List<AirtimeAndDataBeneficiary>,
    onDelete: (AirtimeAndDataBeneficiary) -> Unit,
    onEdit: (AirtimeAndDataBeneficiary) -> Unit,
    onClick: (AirtimeAndDataBeneficiary) -> Unit
) {
    val swipedItemId = remember { mutableStateOf<String?>(null) }

    LazyColumn {
        items(beneficiaries, key = { it.id ?: it.phoneNumber }) { beneficiary ->
            SwipeableItemWise2(
                beneficiary = beneficiary,
                onDelete = onDelete,
                onEdit = onEdit,
                onClick = onClick,
                swipedItemId = swipedItemId,
            )
        }
    }
}

@Composable
fun SwipeableItemWise2(
    beneficiary: AirtimeAndDataBeneficiary,
    onEdit: (AirtimeAndDataBeneficiary) -> Unit,
    onDelete: (AirtimeAndDataBeneficiary) -> Unit,
    onClick: (AirtimeAndDataBeneficiary) -> Unit,
    swipedItemId: MutableState<String?>
) {
    val swipeOffset = remember { Animatable(0f) }
    val maxOffset = 200f
    val coroutineScope = rememberCoroutineScope()

    // Reset swipe if this isn't the currently swiped item
    LaunchedEffect(swipedItemId.value) {
        if (swipedItemId.value != beneficiary.id && swipeOffset.value != 0f) {
            swipeOffset.animateTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        // Background Actions (Edit & Delete)
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Edit Background
            Box(
                modifier = Modifier
                    //.weight(1f)
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(color = Color(0xFF4CAF50), shape = RoundedCornerShape(8.dp)), // Green
                    //.clickable { onEdit(beneficiary) },
                contentAlignment = Alignment.CenterStart
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(start = 30.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
                    Text("Edit", color = Color.White, fontSize = 12.sp)
                }
            }

            // Spacer (rest of the space, if needed)
            Spacer(modifier = Modifier.weight(1f))

            // Delete Background
            Box(
                modifier = Modifier
                    //.weight(1f)
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(color = Color(0xFFF44336), shape = RoundedCornerShape(8.dp)) // Red
                    .clickable { onDelete(beneficiary) },
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    Text("Delete", color = Color.White, fontSize = 12.sp)
                }
            }
        }

        // Foreground Card (swipeable)
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeOffset.value.toInt(), 0) }
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            coroutineScope.launch {
//                                when {
//                                    swipeOffset.value > maxOffset / 2 -> swipeOffset.animateTo(maxOffset)
//                                    swipeOffset.value < -maxOffset / 2 -> swipeOffset.animateTo(-maxOffset)
//                                    else -> swipeOffset.animateTo(0f)
//                                }

                                //click to perform edit and delete actions
//                                if (swipeOffset.value > maxOffset / 2) {
//                                    swipeOffset.animateTo(maxOffset)
//                                    swipedItemId.value = beneficiary.id
//                                } else if (swipeOffset.value < -maxOffset / 2) {
//                                    swipeOffset.animateTo(-maxOffset)
//                                    swipedItemId.value = beneficiary.id
//                                } else {
//                                    swipeOffset.animateTo(0f)
//                                    if (swipedItemId.value == beneficiary.id) {
//                                        swipedItemId.value = null
//                                    }
//                                }

                                //trigger action automatically when swiped
                                if (swipeOffset.value > maxOffset / 2) {
                                    // Swiped right → trigger Edit
                                    onEdit(beneficiary)
                                    swipeOffset.animateTo(0f)
                                    swipedItemId.value = null
                                } else if (swipeOffset.value < -maxOffset / 2) {
                                    // Swiped left → trigger Delete
                                    onDelete(beneficiary)
                                    swipeOffset.animateTo(0f)
                                    swipedItemId.value = null
                                } else {
                                    swipeOffset.animateTo(0f)
                                    if (swipedItemId.value == beneficiary.id) {
                                        swipedItemId.value = null
                                    }
                                }

                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            coroutineScope.launch {
                                val newOffset = (swipeOffset.value + dragAmount).coerceIn(-maxOffset, maxOffset)
                                swipeOffset.snapTo(newOffset)
                            }
                        }
                    )
                }
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth().clickable(onClick = { onClick(beneficiary) }),
            ) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(beneficiary.nickName ?: beneficiary.phoneNumber, fontSize = 16.sp)
                        Text("₦${beneficiary.amount}", fontSize = 14.sp, color = Color.Gray)
                    }
                    Text(beneficiary.network ?: "", fontSize = 14.sp, color = Color.Black)
                }
            }
        }
    }
}
