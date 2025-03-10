package com.emperormoh.myplayground.presentation.screens.airtimedata.airtime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatAmountTextFieldNoLabel
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatRedButton
import com.emperormoh.myplayground.presentation.screens.common.ClickableChip
import com.emperormoh.myplayground.presentation.screens.common.AutoTopUpSwitchCard
import com.emperormoh.myplayground.presentation.screens.common.BeneficiaryInfoCard
import com.emperormoh.myplayground.presentation.screens.common.LabeledCheckbox
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocalAirtimeAccountScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onBotClick: () -> Unit,
    onAmountChanged: (Double) -> Unit,
    onAutoTopUpCheckedChange: (Boolean) -> Unit,
    onSaveBeneficiaryChecked: (Boolean) -> Unit,
    onProceedToPayClick:() -> Unit
){
    val isAutoTopUpChecked by remember { mutableStateOf(false) }
    val isSaveBeneficiaryChecked by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            TopBar(title = "Transaction Summary",
                titleFontSize = 15.sp,
                onBack = {onBackClick()},
                onAction = {onBotClick()})
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                //contentAlignment = Alignment.Center
            ) {
                AutoTopUpSwitchCard(
                    isAutoTopUpCheckedChecked = isAutoTopUpChecked,
                    onAutoTopUpCheckedChange = onAutoTopUpCheckedChange,
                    topUpTypeText = "Your airtime will automatically recharge"
                )
                AlatRedButton(modifier = modifier.fillMaxWidth(),
                    isEnabled = true,
                    text = "Proceed to pay",
                    onClick = onProceedToPayClick
                )
            }
        }
    ){paddingVal ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingVal)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            BeneficiaryInfoCard(titleText = "08064054304", descriptionText = "MtN")
            HorizontalDivider(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
                thickness = 1.dp,
                color = colorResource(R.color.CoreUiBorderColor)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlatGeneralText(text = "Phone Number", fontWeight = 700, fontSize = 12.sp)
                AlatGeneralText(
                    text = "Daily Limit: ₦0.00",
                    fontWeight = 400,
                    fontSize = 12.sp
                )
            }
            var amount by remember {
                mutableDoubleStateOf(0.0)
            }
            AlatAmountTextFieldNoLabel(
                amount = amount,
                onAmountChange = onAmountChanged,
                placeholder = "₦0.00"
            )

            val amountList = listOf("₦100", "₦250", "₦500", "₦1000")
            if(amountList.isNotEmpty()){
                FlowRow{
                    amountList.forEach { it ->

                        ClickableChip(value = it, onSuggestedAmountClicked = {
                            amount = it.replace("₦", "").toDouble()
                            onAmountChanged(amount)
                        })
                        Spacer(modifier.width(10.dp))
                    }
                }
            }
            Spacer(modifier.height(10.dp))
            LabeledCheckbox(
                isChecked = isSaveBeneficiaryChecked,
                label = "Save as beneficiary",
                onCheckedChange = onSaveBeneficiaryChecked
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavedBenCardPreview(){
    MyPlayGroundTheme {
        LocalAirtimeAccountScreen(onBotClick = {},
            onBackClick = {},
            onAmountChanged = {},
            onProceedToPayClick = {},
            onAutoTopUpCheckedChange = {},
            onSaveBeneficiaryChecked = {}
        )
    }
}