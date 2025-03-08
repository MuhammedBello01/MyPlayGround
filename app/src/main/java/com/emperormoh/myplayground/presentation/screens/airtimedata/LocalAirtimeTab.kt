package com.emperormoh.myplayground.presentation.screens.airtimedata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatEditTextField
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatEditTextFieldNoLabel
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatRedButton
import com.emperormoh.myplayground.ui.theme.BankCardBorder
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme


@Composable
fun LocalAirtimeRoute(
    localAirtimeViewModel: LocalAirtimeViewModel,
){
    val uiState by localAirtimeViewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        localAirtimeViewModel.InitModel(context)
    }
    LocalAirtimeTab(
        localAirtimeUiState = uiState,
        onPredictNetwork = { phone ->
            if (phone.isNotBlank()) {
                localAirtimeViewModel.predictPhoneNetwork(uiState.phoneNumber)
            }
        },
        onPhoneNumberChanged = { localAirtimeViewModel.onPhoneNumberChanged(it) }
    )
}
@Composable
fun LocalAirtimeTab(
    modifier: Modifier = Modifier,
    localAirtimeUiState: LocalAirtimeUiState,
    onPhoneNumberChanged: (String) -> Unit,
    onPredictNetwork: (String) -> Unit
){

    var phoneNumber by remember { mutableStateOf("") }
    val isContinueButtonEnable by remember { mutableStateOf(false) }
    val isShowPhoneNumberInputGuideText by remember { mutableStateOf(true) }
    //val isPhoneNumberPredicted by remember { mutableStateOf(true) }
    var selectedNetworkIndex by remember { mutableIntStateOf(-1) } // Holds selected index
    when(localAirtimeUiState.predictedNetworkIndex){
        0 -> selectedNetworkIndex = 2
        1 -> selectedNetworkIndex = 1
        2 -> selectedNetworkIndex = 3
        3 -> selectedNetworkIndex = 0
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Spacer(modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AlatGeneralText(text = "Phone Number", fontWeight = 700, fontSize = 12.sp)
            AlatGeneralText(
                text = "Select From Contacts",
                textColor = colorResource(R.color.CoreUiAlatRed),
                fontWeight = 700,
                fontSize = 12.sp
            )
        }
        AlatEditTextFieldNoLabel(value = phoneNumber,
            onValueChange = {
                phoneNumber = it
                localAirtimeUiState.phoneNumber = it
                if (it.length < 11){
                    onPhoneNumberChanged(it)
                }
                if (it.length == 11){
                    onPredictNetwork(it)
                } },
            placeholder = "Enter phone number",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        if (isShowPhoneNumberInputGuideText){
            AlatGeneralText(text = "Please enter a valid 11-digit phone number",
                fontWeight = 300,
                fontSize = 12.sp,
                textColor = colorResource(R.color.CoreUiTextFieldHint))
        }

        if (localAirtimeUiState.isPhoneNumberPredicted){
            Column {
                Spacer(modifier.height(15.dp))
                AlatGeneralText(text = "Select Network", fontWeight = 700, fontSize = 15.sp)
                Spacer(modifier.height(5.dp))

                SelectableImagesRow(
                    condition = true,
                    defaultSelectedIndex = 2,
                    selectedIndex = selectedNetworkIndex,
                    onSelectionChange = { selectedNetworkIndex = it } // Update selection
                )
            }
        }


        Spacer(modifier.height(30.dp))

        AlatRedButton(modifier = Modifier.fillMaxWidth(),
            text = "Next",
            onClick = {},
            isEnabled = isContinueButtonEnable
        )
        Spacer(modifier.height(30.dp))

        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth(),
                //.padding(horizontal = 1.dp, vertical = 1.dp),
            thickness = 1.dp,
            color = colorResource(R.color.CoreUiTextFieldHint)
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Centers items vertically
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource( R.drawable.ic_beneficiary),
                modifier = Modifier.size(50.dp),
                contentDescription = "icon"
            )
            Spacer(modifier.height(20.dp))
            AlatGeneralText(text = "No beneficiaries yet",
                fontWeight = 500,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier.height(10.dp))
            AlatGeneralText(modifier = modifier.fillMaxWidth(),
                text = "No beneficiaries here yet, your beneficiaries will show up when you make transactions",
                fontWeight = 300,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

        }

    }

}

@Preview(showBackground = true)
@Composable
fun LocalAirtimeTabPreview(){
    MyPlayGroundTheme {
       //LocalAirtimeTab(onPredictNetwork = {})
    }

}