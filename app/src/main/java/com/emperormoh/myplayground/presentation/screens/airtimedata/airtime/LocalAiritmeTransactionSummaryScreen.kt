package com.emperormoh.myplayground.presentation.screens.airtimedata.airtime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.TopBar
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatRedButton
import com.emperormoh.myplayground.presentation.componenets.alat_components.PinTextFieldTwo
import com.emperormoh.myplayground.presentation.screens.common.NameAndValueRow
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@Composable
fun LocalAirtimeTransactionSummaryScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onBotClick: () -> Unit,
    onPinChange: (String) -> Unit,
    onPayClick:() -> Unit
){
    var pin by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            TopBar(title = "Transaction Summary",
                titleFontSize = 15.sp,
                onBack = {onBackClick()},
                onAction = {onBotClick()})
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                AlatRedButton(modifier = modifier.fillMaxWidth(),
                    text = "Proceed to pay",
                    onClick = {}
                )
            }
        }
    ){paddingVal ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingVal)
                .padding(16.dp)
        ){
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = colorResource(R.color.CoreUiSurfaceBackground),
            ){
                Column(
                    modifier = Modifier.padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AlatGeneralText(text = "Total Amount",
                        fontWeight = 400,
                        fontSize = 15.sp,
                        textColor = colorResource(R.color.CoreUiDarkTealColor)
                    )
                    Spacer(modifier.height(5.dp))
                    AlatGeneralText(text = "N100,000.00",
                        fontWeight = 700,
                        fontSize = 25.sp,
                        textColor = colorResource(R.color.CoreUiDarkTealColor)
                    )
                }
            }
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
                thickness = 1.dp,
                color = colorResource(R.color.CoreUiBorderColor)
            )

            NameAndValueRow(nameText = "Phone Number", valueText = "08064054305")
            NameAndValueRow(nameText = "Product", valueText = "Airtime")
            NameAndValueRow(nameText = "Network", valueText = "MTN")
            NameAndValueRow(nameText = "Auto Top-up", valueText = "On")
            NameAndValueRow(nameText = "Top-up When Airtime Is", valueText = "N100")

            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
                thickness = 1.dp,
                color = colorResource(R.color.CoreUiBorderColor)
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                AlatGeneralText(
                    modifier = modifier.padding(bottom = 10.dp), text = "Transaction PIN",
                    fontWeight = 700,
                    fontSize = 15.sp,
                    textColor = colorResource(R.color.CoreUiDarkTealColor)
                )
                PinTextFieldTwo(onValueChange = {
                    pin = it
                    onPinChange(it) },
                    value = pin
                )
            }
//            AlatGeneralText(
//                modifier = modifier.padding(bottom = 10.dp), text = pin,
//                fontWeight = 700,
//                fontSize = 15.sp,
//                textColor = colorResource(R.color.CoreUiDarkTealColor)
//            )


        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocalAirtimeTsPreview() {
    MyPlayGroundTheme {
        LocalAirtimeTransactionSummaryScreen(
            onBackClick = {},
            onBotClick = {},
            onPayClick = {},
            onPinChange = {}
        )
    }
}