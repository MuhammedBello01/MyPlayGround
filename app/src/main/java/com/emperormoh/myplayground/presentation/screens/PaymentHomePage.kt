package com.emperormoh.myplayground.presentation.screens

import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatEditTextField
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

@Composable
fun PaymentHomePage(
    modifier: Modifier = Modifier,
    onOptionClick: (String) -> Unit
){

    var searchQuery by remember { mutableStateOf("") }
    Scaffold(
        topBar = {},

        ) {paddingVal->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingVal)
                .padding(16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AlatGeneralText(text = "Payment", fontWeight = 700, fontSize = 15.sp)
                AlatGeneralText(
                    text = "Manage Beneficiaries",
                    textColor = colorResource(R.color.CoreUiAlatRed),
                    fontWeight = 700,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier.height(15.dp))
            AlatEditTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                placeholder = "Search for payment",
                maxLines = 1,
                label = "",
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                }
            )
            Spacer(modifier.height(10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns per row
                //contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                //modifier = Modifier.fillMaxSize()
            ) {
                items(paymentOptionsList.size) { option ->
                    PayOptionCard(
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                            .fillMaxWidth(),
                        paymentOptions = paymentOptionsList[option],
                        onClick = {
                            when(option){
                                0 -> { onOptionClick("send_money")}
                                1 -> { onOptionClick("bill_payment")}
                                2 -> { onOptionClick("local_Airtime_data")}

                            }
                        }
                    )
                }
            }

        }
    }

}


@Composable
fun PayOptionCard(
    modifier: Modifier = Modifier,
    paymentOptions: PaymentHomeOptions,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.height(120.dp)
            //.padding(10.dp)
            .border(
                width = 1.dp,
                color = Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.CoreUiSurfaceBackground)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ){
        Column(modifier = Modifier.padding(14.dp)) {
            Image(
                painter = painterResource(paymentOptions.icon),
                modifier = Modifier.size(50.dp),
                contentDescription = "icon"
            )
            Spacer(Modifier.height(15.dp))
            AlatGeneralText(text = paymentOptions.title, fontWeight = 500, fontSize = 12.sp)
        }
    }

}

data class PaymentHomeOptions(
    val icon: Int,
    val title: String,
    val uuid: String = UUID.randomUUID().toString()
)

val paymentOptionsList = listOf(
    PaymentHomeOptions(icon = R.drawable.ic_send_money_ph, title = "Send Money"),
    PaymentHomeOptions(icon = R.drawable.ic_bill_payment_ph, title = "Bill Payment"),
    PaymentHomeOptions(icon = R.drawable.ic_local_airtime_data_ph, title = "Local Airtime/Data"),
    PaymentHomeOptions(icon = R.drawable.ic_intl_airtime_data_ph, title = "Int'l Airtime/Data"),
    PaymentHomeOptions(icon = R.drawable.ic_e_sim_ph, title = "eSIM"),
    PaymentHomeOptions(icon = R.drawable.ic_fx_sales_ph, title = "FX Sales"),
    PaymentHomeOptions(icon = R.drawable.ic_nfc_payment_ph, title = "NFC Payments"),
    PaymentHomeOptions(icon = R.drawable.ic_qr_payment_ph, title = "QR Payment"),
    PaymentHomeOptions(icon = R.drawable.ic_evoucher_giftcard_ph, title = "eVoucher/Gift Card"),
    PaymentHomeOptions(icon = R.drawable.ic_remita_ph, title = "Remita"),
    PaymentHomeOptions(icon = R.drawable.ic_bnpl_ph, title = "Buy Now Pay Later"),
    PaymentHomeOptions(icon = R.drawable.ic_western_union_ph, title = "Western Union"),
    PaymentHomeOptions(icon = R.drawable.ic_shedule_payment_ph, title = "Schedule Payment")
)

@Preview(showBackground = true)
@Composable
fun PaymentHomePagePreview(){


    MyPlayGroundTheme {
        PaymentHomePage(onOptionClick = {})
//        PayOptionCard(
//            paymentOptions = paymentOptionsList.first(),
//            onClick = { TODO() },
//        )
    }




}