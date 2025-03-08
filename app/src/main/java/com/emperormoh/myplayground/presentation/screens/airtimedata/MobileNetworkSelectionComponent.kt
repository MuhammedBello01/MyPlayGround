package com.emperormoh.myplayground.presentation.screens.airtimedata

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.R

@Composable
fun SelectableImagesRow(
    condition: Boolean,
    defaultSelectedIndex: Int?,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit
) {
    val images = listOf(
        R.drawable.core_ui_mtn, // Replace with actual drawable resources
        R.drawable.core_ui_airtel,
        R.drawable.core_ui_nine_mobile,
        R.drawable.core_ui_glo
    )

    // Automatically select an image when the condition is met
    LaunchedEffect(condition) {
        if (condition && defaultSelectedIndex != null) {
            onSelectionChange(defaultSelectedIndex)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // Reduce spacing
        //verticalAlignment = Alignment.CenterVertically
    ) {
        images.forEachIndexed { index, imageRes ->
            ImageItem(
                imageRes = imageRes,
                isSelected = selectedIndex == index,
                onClick = {
                    onSelectionChange(index)
                }
            )
        }
    }
}

@Composable
fun ImageItem(imageRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp) // Set fixed size
            .clip(CircleShape) // Make it circular
            .border(
                width = if (isSelected) 2.dp else 0.dp, // Apply red border if selected
                color = if (isSelected) Color.Red else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() }
            .padding(3.dp)// Allow user to select
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape) // Ensure the image stays circular
        )
        if(isSelected){
            Image(
                painter = painterResource(R.drawable.core_ui_checked),
                contentDescription = "Selected",
                modifier = Modifier
                    .size(80.dp) // Set icon size
                    .padding(18.dp) // Padding inside background
            )
        }
    }
}

@Composable
fun ParentComposable() {
    var selectedIndex by remember { mutableIntStateOf(-1) } // Holds selected index

    SelectableImagesRow(
        condition = true,
        defaultSelectedIndex = 2,
        selectedIndex = selectedIndex,
        onSelectionChange = { selectedIndex = it } // Update selection
    )

    Text(
        text = "Selected Index: $selectedIndex",
        modifier = Modifier.padding(top = 16.dp)
    )
}

