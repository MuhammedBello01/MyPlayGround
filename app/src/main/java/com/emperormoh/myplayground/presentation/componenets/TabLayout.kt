package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


val  tabRowItems = listOf(
    TabRowItem(
        enabled = false, title = {Text(text = "Frequent")},
        screen = { TODO() }
    ),
    TabRowItem(
        enabled = false, title = {Text(text = "Saved")},
        screen = { TODO() }
    ),

)

data class TabRowItem(
    val enabled: Boolean = true,
    val title: @Composable () -> Unit,
    val screen: @Composable () -> Unit,
)