package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StepIndicator(
    steps: Int,
    currentStep: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        repeat(steps) { index ->
            StepItem(isCompleted = index < currentStep, isCurrent = index == currentStep)
            if (index < steps - 1) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(if (index < currentStep) Color.Green else Color.Gray)
                )


            }
        }
    }
}

@Composable
fun StepItem(isCompleted: Boolean, isCurrent: Boolean) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                when {
                    isCompleted -> Color.Green
                    isCurrent -> Color.Blue
                    else -> Color.Gray
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color.White
            )
        } else {
            Text(
                text = if (isCurrent) "•" else "",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun StepIndicatorScreen() {
    var currentStep by remember { mutableIntStateOf(0) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        StepIndicator(steps = 5, currentStep = currentStep)

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Button(onClick = { if (currentStep > 0) currentStep-- }) {
                Text("Previous")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { if (currentStep < 4) currentStep++ }) {
                Text("Next")
            }
        }
    }
}
