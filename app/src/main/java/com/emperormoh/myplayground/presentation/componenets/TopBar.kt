package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    titleFontSize: TextUnit = 20.sp,
    onBack: (() -> Unit)? = null,
    onAction: (() -> Unit)? = null
){
    CenterAlignedTopAppBar(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .wrapContentHeight()
        ,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            onBack?.let {
                Image(
                    painter = painterResource(R.drawable.core_ui_arrow_left),
                    contentDescription = null,
                    colorFilter = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { it() }
                        .testTag("back_button")
                )
            }

        },
        title = {
            AlatGeneralText(text = title, fontWeight = 700, fontSize = titleFontSize)
        },
        actions = {
            onAction?.let {
                Image(
                    painter = painterResource(R.drawable.core_ui_bot_msg),
                    contentDescription = null,
                    colorFilter = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { it() }
                        .testTag("bot_click")
                )
            }

        })
}

@Composable
@Preview(showBackground = true)
fun TopBarPreview(){
    TopBar("Payment", onBack = {}, onAction = {})
}