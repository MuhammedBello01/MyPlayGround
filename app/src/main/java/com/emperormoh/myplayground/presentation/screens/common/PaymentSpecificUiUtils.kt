package com.emperormoh.myplayground.presentation.screens.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@Composable
fun NameAndValueRow(
    modifier: Modifier = Modifier,
    nameText: String,
    valueText: String,
    nameTextColor: Color = colorResource(R.color.CoreUiTextFieldHint),
    nameTextSize: TextUnit = 14.sp,
    nameFontWeight: Int = 400,
    valueColor: Color = colorResource(R.color.CoreUiBlackToWhite),
    valueTextSize: TextUnit = 14.sp,
    valueFontWeight: Int = 500
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.4f)
        ) {
            AlatGeneralText(
                text = nameText,
                fontWeight = nameFontWeight,
                fontSize = nameTextSize,
                textColor = nameTextColor
            )
        }
        Column(
            modifier = Modifier.weight(0.6f)
        ) {
            AlatGeneralText(
                modifier = modifier.align(Alignment.End),
                text = valueText,
                textColor = valueColor,
                fontWeight = valueFontWeight,
                fontSize = valueTextSize
            )
        }
    }
}

@Composable
fun BeneficiaryInfoCard(
    modifier: Modifier = Modifier,
    titleText: String,
    descriptionText: String,
    isShowArrow: Boolean = true,
    imageRes: Int = R.drawable.core_ui_mtn
){
    Box(
        modifier = Modifier
            .background(color = colorResource(R.color.CoreUiSurfaceBackground),
                shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ){
        Row(modifier = modifier.padding(16.dp)){
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.align(alignment = Alignment.CenterVertically)
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier.width(10.dp))
            Column(
                modifier.align(alignment = Alignment.CenterVertically)
            ){
                AlatGeneralText(text = "Phone Number",
                    fontWeight = 500,
                    fontSize = 14.sp
                )
                AlatGeneralText(text = "MTN",
                    fontWeight = 400,
                    fontSize = 12.sp,
                    textColor = colorResource(R.color.CoreUiTextFieldHint)
                )
            }
        }
    }
}

@Composable
fun AutoTopUpSwitchCard(
    modifier: Modifier = Modifier,
    isAutoTopUpCheckedChecked: Boolean = false,
    topUpTypeText: String,
    onAutoTopUpCheckedChange: (Boolean) -> Unit
){
    //var isChecked by remember { mutableStateOf(isAutoTopUpCheckedChecked) }
    Box(
        modifier = Modifier
            .background(color = colorResource(R.color.CoreUiSurfaceBackground),
                shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ){
        Row(
            modifier = modifier.fillMaxWidth().padding(16.dp),
            //verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column{
                AlatGeneralText(text = "Auto top up",
                    fontWeight = 500,
                    fontSize = 15.sp,
                    textColor = colorResource(R.color.CoreUiTextColor)
                )
                AlatGeneralText(text = topUpTypeText,
                    fontWeight = 400,
                    fontSize = 12.sp,
                    textColor = colorResource(R.color.CoreUiDarkTealColor)
                )
            }
            Switch(
                checked = isAutoTopUpCheckedChecked,
                onCheckedChange = onAutoTopUpCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(R.color.CoreUiBackgroundColor),
                    uncheckedThumbColor = colorResource(R.color.CoreUiBackgroundColor),
                    checkedTrackColor = colorResource(R.color.CoreUiAlatRed),
                    uncheckedTrackColor = colorResource(R.color.CoreUiTextFieldHint)
                ),
                thumbContent = {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(color = colorResource(R.color.CoreUiBackgroundColor), shape = CircleShape)
                    )
                }
            )
        }
    }
}

@Composable
fun ClickableChip(
    modifier: Modifier = Modifier,
    value: String = "₦0.00",
    onSuggestedAmountClicked: (String) -> Unit
){
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .wrapContentSize()
            .background(
                color = colorResource(R.color.CoreUiSurfaceBackground),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = colorResource(R.color.CoreUiBorderColor)
            )
            .clickable { onSuggestedAmountClicked(value) },
        contentAlignment = Alignment.Center
    ){
        AlatGeneralText(
            modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            text = value,
            fontWeight = 400,
            fontSize = 12.sp
        )
    }
}

@Composable
fun LabeledCheckbox(
    modifier: Modifier = Modifier,
    label: String,
    isChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        //modifier = Modifier.padding(16.dp)
    ) {
        Checkbox(
            modifier = Modifier
                .size(32.dp) // Ensures the size is controlled
                .padding(0.dp),
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.CoreUiAlatRed),
                uncheckedColor = Color.Gray,
                checkmarkColor = colorResource(R.color.CoreUiBackgroundColor),
                disabledCheckedColor = colorResource(R.color.CoreUiBorderColor),
                disabledUncheckedColor =colorResource(R.color.CoreUiBorderColor)
            )
        )
        Spacer(modifier = modifier.width(8.dp))
        AlatGeneralText(
            text = label,
            fontWeight = 500,
            fontSize = 15.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun UiPreviews() {
    MyPlayGroundTheme {

        Column {
           NameAndValueRow(nameText = "Phone Number", valueText = "08064054305")
            BeneficiaryInfoCard(titleText = "08064054304", descriptionText = "MtN")
            AutoTopUpSwitchCard(
                topUpTypeText = "Your airtime will automatically recharge",
                onAutoTopUpCheckedChange = {}
            )
            ClickableChip(onSuggestedAmountClicked = {})
            LabeledCheckbox(label = "Save Beneficiary", onCheckedChange = {}, isChecked = false)
        }

    }
}