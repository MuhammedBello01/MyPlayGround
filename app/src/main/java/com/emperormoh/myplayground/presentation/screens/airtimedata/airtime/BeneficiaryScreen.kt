package com.emperormoh.myplayground.presentation.screens.airtimedata.airtime

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.emperormoh.myplayground.presentation.componenets.IdleDetector
import com.emperormoh.myplayground.presentation.componenets.alat_components.SpeechToTextScreen
import java.math.BigDecimal
import kotlin.math.roundToInt

data class AirtimeAndDataBeneficiary(
    val id: String?,
    val phoneNumber: String,
    val network: String?,
    val amount: BigDecimal =  BigDecimal.ZERO,
    val nickName: String?,
    val transactionType: Int,
    val cif: String,
    val datapackageId: Int = 0
)

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BeneficiaryList(
//    beneficiaries: List<AirtimeAndDataBeneficiary>,
//    onDelete: (AirtimeAndDataBeneficiary) -> Unit,
//    onEdit: (AirtimeAndDataBeneficiary) -> Unit,
//    onClick: (AirtimeAndDataBeneficiary) -> Unit
//) {
//    LazyColumn {
//        items(
//            items = beneficiaries,
//            key = { it.id ?: it.phoneNumber }
//        ) { beneficiary ->
//            val dismissState = rememberSwipeToDismissBoxState(
//                confirmValueChange = { dismissValue ->
//                    when (dismissValue) {
//                        SwipeToDismissBoxValue.StartToEnd -> {
//                            onEdit(beneficiary)
//                            false // Don't dismiss - just reveal action
//                        }
//                        SwipeToDismissBoxValue.EndToStart -> {
//                            onDelete(beneficiary)
//                            false // Don't dismiss - just reveal action
//                        }
//                        SwipeToDismissBoxValue.Settled -> true
//                    }
//                }
//            )
//
//            SwipeToDismissBox(
//                state = dismissState,
//                enableDismissFromStartToEnd = true,
//                enableDismissFromEndToStart = true,
//                backgroundContent = {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(MaterialTheme.colorScheme.surface)
//                    ) {
//                        // Use currentValue to determine swipe direction instead of offset
//                        when (dismissState.currentValue) {
//                            SwipeToDismissBoxValue.StartToEnd -> {
//                                EditBackground()
//                            }
//                            SwipeToDismissBoxValue.EndToStart -> {
//                                DeleteBackground()
//                            }
//                            SwipeToDismissBoxValue.Settled -> {
//                                // No background when settled
//                            }
//                        }
//                    }
//                },
//                content = {
//                    BeneficiaryListItem(beneficiary = beneficiary, onClick = { onClick(beneficiary) })
//                }
//            )
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryList(
    beneficiaries: List<AirtimeAndDataBeneficiary>,
    onDelete: (AirtimeAndDataBeneficiary) -> Unit,
    onEdit: (AirtimeAndDataBeneficiary) -> Unit,
    onClick: (AirtimeAndDataBeneficiary) -> Unit
) {
    LazyColumn {
        items(beneficiaries, key = { it.id ?: it.phoneNumber }) { beneficiary ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { false } // Never dismiss automatically
            )

            // Track swipe direction
            val isSwipedLeft = dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
            val isSwipedRight = dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd

            Box(modifier = Modifier.fillMaxWidth()) {
                // Background actions (shown when swiped)
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Edit action (left side)
                    if (isSwipedRight) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .fillMaxHeight()
                                .background(Color.Blue)
                                .clickable { onEdit(beneficiary) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(80.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Delete action (right side)
                    if (isSwipedLeft) {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .fillMaxHeight()
                                .background(Color.Red)
                                .clickable { onDelete(beneficiary) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                }

                // Main content
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = true,
                    enableDismissFromEndToStart = true,
                    backgroundContent = { /* Empty - we handle background ourselves */ },
                    content = {
                        BeneficiaryListItem(
                            beneficiary = beneficiary,
                            onClick = { onClick(beneficiary) }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun BeneficiaryListItem(
    beneficiary: AirtimeAndDataBeneficiary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // ... rest of your list item content ...
    }
}

@Composable
private fun EditBackground() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            tint = Color.White
        )
    }
}

@Composable
private fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.White
        )
    }
}

@Composable
private fun BeneficiaryListItem(
    beneficiary: AirtimeAndDataBeneficiary,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = beneficiary.nickName ?: "No nickname",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = beneficiary.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Amount: ${beneficiary.amount}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = beneficiary.network ?: "Unknown network",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

val mockAirtimeBeneficiaries = listOf(
    AirtimeAndDataBeneficiary(id = "463765-367345", phoneNumber = "08031234567", network = "MTN", amount = BigDecimal("500.00"), nickName = "John", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "45672-4346744", phoneNumber = "08145678901", network = "Glo", amount = BigDecimal("750.00"), nickName = "Sarah", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-58735358", phoneNumber = "07087654321", network = "Airtel", amount = BigDecimal("1200.00"), nickName = "Michael", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-58735635", phoneNumber = "09098765432", network = "9mobile", amount = BigDecimal("900.00"), nickName = "Lisa", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-58173535", phoneNumber = "08022223333", network = "MTN", amount = BigDecimal("1000.00"), nickName = "David", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-58735380905", phoneNumber = "08133334444", network = "Glo", amount = BigDecimal("850.00"), nickName = "Emma", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-585573535", phoneNumber = "07055556666", network = "Airtel", amount = BigDecimal("650.00"), nickName = "Chris", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-589873535", phoneNumber = "09077778888", network = "9mobile", amount = BigDecimal("700.00"), nickName = "Sophia", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-5872323535", phoneNumber = "08099990000", network = "MTN", amount = BigDecimal("1100.00"), nickName = "Daniel", transactionType = 1, cif = "R005230241"),
    AirtimeAndDataBeneficiary(id = "46573-587133535", phoneNumber = "08111112222", network = "Glo", amount = BigDecimal("1300.00"), nickName = "Grace", transactionType = 1, cif = "R005230241")
)

@Composable
fun BeneficiaryScreen(){
    val context = LocalContext.current
//    BeneficiaryList5(
//        beneficiaries = mockAirtimeBeneficiaries,
//        onDelete = { beneficiary ->
//            //viewModel.deleteBeneficiary(beneficiary)
//            Toast.makeText(context, "Delete ${beneficiary.nickName}", Toast.LENGTH_SHORT).show()
//        },
//        onEdit = { beneficiary ->
//            //showEditDialog = beneficiary
//            Toast.makeText(context, "Edit ${beneficiary.nickName}", Toast.LENGTH_SHORT).show()
//        },
//        onClick = { beneficiary ->
//            // Handle item click
//            Toast.makeText(context, "Clicked ${beneficiary.nickName}", Toast.LENGTH_SHORT).show()
//        }
//    )
    var showIdleDialog by remember { mutableStateOf(false) }

    IdleDetector(
        onIdle = { showIdleDialog = true }
    ) {
        // Your actual UI content here
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Touch the screen to stay active!")
        }

        if (showIdleDialog) {
            AlertDialog(
                onDismissRequest = { showIdleDialog = false },
                title = { Text("Inactive") },
                text = { Text("You've been idle for 60 seconds.") },
                confirmButton = {
                    TextButton(onClick = { showIdleDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
    SpeechToTextScreen()

//    BeneficiaryListWise(
//        beneficiaries = mockAirtimeBeneficiaries,
//        onDelete = { beneficiary ->
//            Toast.makeText(context, "Delete ${beneficiary.nickName}", Toast.LENGTH_SHORT).show()
//        },
//        onEdit = { beneficiary ->
//            Toast.makeText(context, "Edit ${beneficiary.nickName}", Toast.LENGTH_SHORT).show()
//        },
//        onClick = {beneficiary ->
//            Toast.makeText(context, "Clicked ${beneficiary.nickName}", Toast.LENGTH_SHORT).show()}
//    )
}
