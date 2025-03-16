package com.emperormoh.myplayground.presentation.screens.airtimedata.data

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.TopBar
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatAmountTextFieldNoLabel
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatEditTextFieldNoLabel
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatOpenDropDownClick
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatRedButton
import com.emperormoh.myplayground.presentation.screens.common.AutoTopUpSwitchCard
import com.emperormoh.myplayground.presentation.screens.common.BeneficiaryInfoCard
import com.emperormoh.myplayground.presentation.screens.common.ClickableChip
import com.emperormoh.myplayground.presentation.screens.common.LabeledCheckbox
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocalDataAccountScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onBotClick: () -> Unit,
    onAmountChanged: (Double) -> Unit,
    onAutoTopUpCheckedChange: (Boolean) -> Unit,
    onSaveBeneficiaryChecked: (Boolean) -> Unit,
    onBeneficiaryNameChanged: (String) -> Unit,
    onTopUpAmountChanged: (Double) -> Unit,
    onProceedToActivateAutoTopUpClick:() -> Unit,
    onDropDownValueChanged:(String) -> Unit,
    onProceedToPayClick:() -> Unit
){
    val isAutoTopUpChecked by remember { mutableStateOf(false) }
    val isSaveBeneficiaryChecked by remember { mutableStateOf(false) }
    val selectedSuggestedDataMbChip by remember { mutableStateOf<String?>(null) }
    val selectedTopUpAmountChip by remember { mutableStateOf<String?>(null) }
    val topUpTypeText by remember { mutableStateOf("Your data will automatically recharge") }
    var isShowDialog by remember { mutableStateOf(false) }

    var amount by remember {
        mutableDoubleStateOf(0.0)
    }
    var thresholdAmount by remember {
        mutableDoubleStateOf(0.0)
    }

    Scaffold (
        topBar = {
            TopBar(title = "Buy Airtime",
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
                    topUpTypeText = topUpTypeText
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
                AlatGeneralText(text = "Data Bundle", fontWeight = 700, fontSize = 12.sp)
                AlatGeneralText(
                    text = "Daily Limit: ₦0.00",
                    fontWeight = 400,
                    fontSize = 12.sp
                )
            }
            AlatOpenDropDownClick(value = "",
                onValueChange = {
                    onDropDownValueChanged(it)
                },
                placeholder = "Select data bundle",
                onClick = {}
            )
            val dataMbList = listOf("100MB", "250MB", "500MB", "1000MB")
            if(dataMbList.isNotEmpty()){
                FlowRow{
                    dataMbList.forEach { value ->
                        ClickableChip(
                            value = value,
                            isSelected = value == selectedSuggestedDataMbChip,
                            onSuggestedAmountClicked = { amount = it.replace("MB", "").toDouble()
                                onAmountChanged(amount)
                            })
                        Spacer(modifier.width(10.dp))
                    }
                }
            }
            AlatGeneralText(text = "Amount",
                fontWeight = 700,
                fontSize = 12.sp
            )
            AlatAmountTextFieldNoLabel(
                amount = thresholdAmount,
                onAmountChange = onTopUpAmountChanged,
                placeholder = "₦0.00"
            )
            Spacer(modifier.height(10.dp))
            LabeledCheckbox(
                isChecked = isSaveBeneficiaryChecked,
                label = "Save as beneficiary",
                onCheckedChange = onSaveBeneficiaryChecked
            )
            Spacer(modifier.height(15.dp))

            if (isSaveBeneficiaryChecked){
                AlatGeneralText(text = "Beneficiary Name",
                    fontWeight = 700,
                    fontSize = 12.sp
                )
                AlatEditTextFieldNoLabel(value = "",
                    onValueChange = {
                        if (it.isNotBlank()){
                            onBeneficiaryNameChanged(it)
                        } },
                    placeholder = "Enter a nickname",
                )
            }
            if (isShowDialog){
                ModalBottomSheet(onDismissRequest = { isShowDialog = false }){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            AlatGeneralText(text = "Auto top up",
                                fontWeight = 700,
                                fontSize = 25.sp
                            )
                            Image(
                                painter = painterResource(id = R.drawable.core_ui_close),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = modifier.size(30.dp)
                            )
                        }
                        Spacer(Modifier.height(30.dp))
                        AlatGeneralText(
                            text = stringResource(R.string.data_top_up_disclaimer),
                            fontWeight = 400,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(30.dp))
                        AlatGeneralText(text = "Top-up When Data Is",
                            fontWeight = 700,
                            fontSize = 12.sp
                        )
                        AlatEditTextFieldNoLabel(value = "",
                            onValueChange = {
                                if (it.isNotBlank()){
                                    onTopUpAmountChanged(it.toDouble())
                                } },
                            placeholder = "Enter a nickname",
                            suffixText = "MB"
                        )
                        val thresholdDataMbList = listOf("100MB", "250MB", "500MB", "1000MB")
                        if(thresholdDataMbList.isNotEmpty()){
                            FlowRow{
                                thresholdDataMbList.forEach { value ->
                                    ClickableChip(
                                        value = value,
                                        isSelected = value == selectedTopUpAmountChip,
                                        onSuggestedAmountClicked = { thresholdAmount = it.replace("MB", "").toDouble()
                                            onTopUpAmountChanged(thresholdAmount)
                                        })
                                    Spacer(modifier.width(10.dp))
                                }
                            }
                        }
                        Spacer(Modifier.height(30.dp))
                        AlatRedButton(modifier = modifier.fillMaxWidth(),
                            isEnabled = true,
                            text = "Proceed to activate",
                            onClick = onProceedToActivateAutoTopUpClick
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DataTopUpDialog(
    modifier: Modifier = Modifier,
    onTopUpAmountChanged: (Double) -> Unit,
    onProceedToActivateAutoTopUpClick:() -> Unit
){
    val selectedTopUpAmountChip by remember { mutableStateOf<String?>(null) }
    var thresholdAmount by remember {
        mutableDoubleStateOf(0.0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AlatGeneralText(text = "Auto top up",
                fontWeight = 700,
                fontSize = 25.sp
            )
            Image(
                painter = painterResource(id = R.drawable.core_ui_close),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.size(30.dp)
            )
        }
        Spacer(Modifier.height(30.dp))
        AlatGeneralText(
            text = stringResource(R.string.data_top_up_disclaimer),
            fontWeight = 400,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(30.dp))
        AlatGeneralText(text = "Top-up When Data Is",
            fontWeight = 700,
            fontSize = 12.sp
        )
        AlatEditTextFieldNoLabel(value = "",
            onValueChange = {
                if (it.isNotBlank()){
                    onTopUpAmountChanged(it.toDouble())
                } },
            placeholder = "MB",
            suffixText = "MB"
        )
        val thresholdDataMbList = listOf("100MB", "250MB", "500MB", "1000MB")
        if(thresholdDataMbList.isNotEmpty()){
            FlowRow{
                thresholdDataMbList.forEach { value ->
                    ClickableChip(
                        value = value,
                        isSelected = value == selectedTopUpAmountChip,
                        onSuggestedAmountClicked = { thresholdAmount = it.replace("MB", "").toDouble()
                            onTopUpAmountChanged(thresholdAmount)
                        })
                    Spacer(modifier.width(10.dp))
                }
            }
        }
        Spacer(Modifier.height(30.dp))
        AlatRedButton(modifier = modifier.fillMaxWidth(),
            isEnabled = true,
            text = "Proceed to activate",
            onClick = onProceedToActivateAutoTopUpClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocalDataAccountPreview(){
    MyPlayGroundTheme {
        LocalDataAccountScreen(onBotClick = {},
            onBackClick = {},
            onAmountChanged = {},
            onProceedToPayClick = {},
            onAutoTopUpCheckedChange = {},
            onSaveBeneficiaryChecked = {},
            onBeneficiaryNameChanged = {},
            onProceedToActivateAutoTopUpClick = {},
            onTopUpAmountChanged = {},
            onDropDownValueChanged = {}
        )

//        DataTopUpDialog(
//            onProceedToActivateAutoTopUpClick = {},
//            onTopUpAmountChanged = {},
//
//        )
    }
}