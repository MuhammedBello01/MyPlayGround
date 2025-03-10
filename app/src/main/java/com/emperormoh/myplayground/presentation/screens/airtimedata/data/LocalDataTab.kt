package com.emperormoh.myplayground.presentation.screens.airtimedata.data

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatEditTextFieldNoLabel
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatRedButton
import com.emperormoh.myplayground.presentation.screens.airtimedata.NetworkSelectableImagesRow

@Composable
fun LocalDataTab(
    modifier: Modifier = Modifier,
    localDataUiState: LocalDataUiState,
    onPhoneNumberChanged: (String) -> Unit,
    onPredictNetwork: (String) -> Unit,
    onNetworkSelectionChanged: (Int) -> Unit,
    onNavigateToDataAmountRoute: (String) -> Unit
){

    val context = LocalContext.current
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    //var phoneNumber by remember { mutableStateOf("") }
    val isContinueButtonEnable by remember { mutableStateOf(false) }
    val isShowPhoneNumberInputGuideText by remember { mutableStateOf(true) }
    var selectedNetworkIndex by remember { mutableIntStateOf(-1) } // Holds selected index

    Column(
        modifier = modifier
            .fillMaxSize()
    ){
        val startForResult =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    var cursor: Cursor? = null
                    try {
                        val phoneNo: String?
                        val uri: Uri? = result.data?.data
                        cursor =
                            context.contentResolver?.query(uri!!, null, null, null, null)
                        cursor?.moveToFirst()
                        val phoneIndex: Int =
                            cursor!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        phoneNo = cursor.getString(phoneIndex)
                        if (phoneNo != null) {

                            if (phoneNo.startsWith("+")) {
                                phoneNumber.value = "0".plus(
                                    phoneNo
                                        .replace(" ", "")
                                        .replace("-", "")
                                        .removeRange(0..3)
                                )
                            } else {
                                phoneNumber.value = phoneNo
                                    .replace(" ", "")
                                    .replace("-", "")

                            }
                            onPhoneNumberChanged(phoneNumber.value)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    cursor?.close()
                }
            }
        Spacer(modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AlatGeneralText(text = "Phone Number", fontWeight = 700, fontSize = 12.sp)
            AlatGeneralText(
                text = "Select From Contacts",
                textColor = colorResource(R.color.CoreUiAlatRed),
                fontWeight = 700,
                fontSize = 12.sp
            )
        }
        AlatEditTextFieldNoLabel(value =  localDataUiState.phoneNumber,
            onValueChange = {
                //phoneNumber = it
                //localDataUiState.phoneNumber = it
                if (it.length < 11){
                    onPhoneNumberChanged(it)
                }
                if (it.isNotBlank() && it.length == 11){
                    onPredictNetwork(it)
                } },
            placeholder = "Enter phone number",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        if (isShowPhoneNumberInputGuideText){
            AlatGeneralText(text = "Please enter a valid 11-digit phone number",
                fontWeight = 300,
                fontSize = 12.sp,
                textColor = colorResource(R.color.CoreUiTextFieldHint)
            )
        }

        if (localDataUiState.isPhoneNumberPredicted){
            Column {
                Spacer(modifier.height(15.dp))
                AlatGeneralText(text = "Select Network", fontWeight = 700, fontSize = 15.sp)
                Spacer(modifier.height(5.dp))

                NetworkSelectableImagesRow(
                    condition = true,
                    defaultSelectedIndex = localDataUiState.predictedNetworkIndex,
                    selectedIndex = selectedNetworkIndex,
                    onSelectionChange = {
                        selectedNetworkIndex = it
                        onNetworkSelectionChanged(it)
                    }
                )
            }
        }

        AlatRedButton(modifier = Modifier.fillMaxWidth(),
            text = "Continue",
            onClick = { onNavigateToDataAmountRoute("data_amount_route")},
            isEnabled = isContinueButtonEnable
        )

        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 30.dp),
            thickness = 1.dp,
            color = colorResource(R.color.CoreUiBorderColor)
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