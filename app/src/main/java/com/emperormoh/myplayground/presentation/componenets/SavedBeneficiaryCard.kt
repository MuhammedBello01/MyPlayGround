package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.screens.SpaceHeight
import com.emperormoh.myplayground.presentation.screens.SpaceWidth
import com.emperormoh.myplayground.ui.theme.BankCardBorder
import com.emperormoh.myplayground.ui.theme.BankGray
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.TextFieldBG

data class TransferBeneficiaryResponse(
    val beneficiaries: List<TransferBeneficiary>,
    val pageNumber: Int,
    val pageSize: Int
)

data class TransferBeneficiary(
    val id: String,
    val destinationAccountNumber: String,
    val destinationAccountName: String,
    val destinationBankName: String,
    val destinationBankCode: String,
    val nickName: String,
    val currency: String,
    val beneficiaryBankLogo: String
)

// List of sample names for random selection
val sampleBeneficiaryNames = listOf(
    "John Doe", "Jane Smith", "Michael Johnson", "Emily Davis", "David Brown",
    "Sarah Wilson", "James Taylor", "Olivia Martinez", "Daniel Anderson",
    "Sophia Thomas", "Scarlett Baker", "Jackson Nelson", "Grace Mitchell",
    "Olu Bolakale", "Emmanuel Smith", "Taiwo Ojo", "Johnson Oyesina"
)
val sampleFrequentNames = listOf(
    "Ethan Walker", "Isabella Carter", "Liam Scott", "Ava Turner", "Noah Adams",
    "Mia Phillips", "Lucas Harris", "Charlotte Roberts", "Mason Clark", "Amelia Lewis",
    "Benjamin Hall", "Harper Young", "Elijah Allen", "Ella King", "Alexander Wright",
    "Sofia Hill", "Henry Green"
)

// Helper function to generate a random 10-digit account number
fun generateRandomAccountNumber(): String {
    return (1000000000..9999999999).random().toString()
}

// Function to generate a mock TransferBeneficiaryResponse
fun getMockTransferBeneficiaryResponse(): TransferBeneficiaryResponse {
    val beneficiaries = allBanks.mapIndexed { index, bank ->
        TransferBeneficiary(
            id = "BEN$index",
            destinationAccountNumber = generateRandomAccountNumber(),
            destinationAccountName = sampleBeneficiaryNames.random(), // Selects a random name
            destinationBankName = bank.bankName,
            destinationBankCode = bank.bankCode,
            nickName = sampleBeneficiaryNames.random().split(" ")[0], // Uses the first name as a nickname
            currency = "NGN",
            beneficiaryBankLogo = bank.bankLogo
        )
    }

    return TransferBeneficiaryResponse(
        beneficiaries = beneficiaries,
        pageNumber = 1,
        pageSize = beneficiaries.size
    )
}

fun getMockTransferFrequentBeneficiaryResponse(): TransferBeneficiaryResponse {
    val beneficiaries = allBanks.mapIndexed { index, bank ->
        TransferBeneficiary(
            id = "BEN$index",
            destinationAccountNumber = generateRandomAccountNumber(),
            destinationAccountName = sampleFrequentNames.random(), // Selects a random name
            destinationBankName = bank.bankName,
            destinationBankCode = bank.bankCode,
            nickName = sampleBeneficiaryNames.random().split(" ")[0], // Uses the first name as a nickname
            currency = "NGN",
            beneficiaryBankLogo = bank.bankLogo
        )
    }

    return TransferBeneficiaryResponse(
        beneficiaries = beneficiaries,
        pageNumber = 1,
        pageSize = beneficiaries.size
    )
}

@Composable
fun SavedBeneficiaryCard(
    modifier: Modifier = Modifier,
    beneficiary: TransferBeneficiary,
    isShowArrow: Boolean = true,
    onBeneficiaryClicked: () -> Unit
){
    Box(
        modifier = Modifier
            .clickable { onBeneficiaryClicked() }
            .background(color = TextFieldBG, shape = RoundedCornerShape(8.dp))
            //.wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row {
            LoadImageFromUrlOpt(
                modifier.background(
                    color = BankGray,
            shape = CircleShape)
            .border(width = 0.5.dp, color = BankCardBorder, shape = CircleShape)
            .size(50.dp).clip(CircleShape),
                model = beneficiary.beneficiaryBankLogo,
                contentDescription = beneficiary.destinationBankName,
                placeholderIcon = { Icon(modifier = Modifier.fillMaxSize()
                    .padding(4.dp),
                    painter = painterResource(id = R.drawable.ic_bank),
                    contentDescription = null)
                }
            )
            SpaceWidth(10.dp)
            Column(
                modifier.align(alignment = Alignment.CenterVertically)
            ) {
                Text(text = beneficiary.destinationAccountName, style = TextStyle(
                    fontFamily = Manrope,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                    color = Color.Black,
                    letterSpacing = 0.2.sp)
                )
                SpaceHeight(5.dp)
                Text(text = "${beneficiary.destinationAccountNumber} | ${beneficiary.destinationBankName}", style = TextStyle(
                    fontFamily = Manrope,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    color = Color.Black,
                    letterSpacing = 0.2.sp))
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
                .align(Alignment.CenterEnd),
            visible = isShowArrow,
            enter = slideInHorizontally() + expandVertically(),
            exit = slideOutHorizontally() + shrinkVertically()
        ){
            Image(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "content image",
                colorFilter = ColorFilter.tint(color = Color.Gray),
                contentScale = ContentScale.FillBounds
            )
        }


    }

}

@Preview(showBackground = true)
@Composable
fun SavedBenCardPreview(){
    MyPlayGroundTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            SavedBeneficiaryCard(isShowArrow = true, onBeneficiaryClicked = {}, beneficiary =getMockTransferBeneficiaryResponse().beneficiaries.first() )

        }
    }
}