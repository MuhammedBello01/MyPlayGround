package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerticalStepIndicator(steps: List<TransactionStep>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        steps.forEachIndexed { index, step ->

            val indicatorColor = when (step.status) {
                StepStatus.COMPLETED -> Color(0xFF006400)
                StepStatus.PENDING -> Color(0xFFEAA300)
                StepStatus.NOT_YET_STARTED -> Color.Gray
            }
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Step Indicator
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(width = 1.dp, color = indicatorColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
//                        if (step.status == StepStatus.COMPLETED) {
//
//                        }
                        Box(
                            modifier = Modifier.padding(5.dp)
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(indicatorColor)
                        )
                    }

                    // Connector Line
                    if (index < steps.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(40.dp)
                                .background(indicatorColor)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Step Details
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = step.message,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = step.timeAndDate,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun VerticalStepIndicatorScreen() {
    val steps = listOf(
        TransactionStep(StepStatus.COMPLETED, "Order Placed", "2024-02-01 10:30 AM"),
        TransactionStep(StepStatus.PENDING, "Payment Confirmed", "2024-02-01 10:35 AM"),
        TransactionStep(StepStatus.COMPLETED, "Shipped", "Awaiting shipment"),
        TransactionStep(StepStatus.NOT_YET_STARTED, "Out for Delivery", "Scheduled for 2024-02-05"),
                TransactionStep(StepStatus.NOT_YET_STARTED, "Out for Delivery", "Scheduled for 2024-02-05")

    )

    Column(modifier = Modifier.padding(top = 100.dp)) {
        val te = steps.sortedBy { it.status }
        VerticalStepIndicator(steps = te)
    }


}
