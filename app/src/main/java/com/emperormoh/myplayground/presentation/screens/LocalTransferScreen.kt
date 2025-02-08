package com.emperormoh.myplayground.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.BankItemCardOne
import com.emperormoh.myplayground.presentation.componenets.BankNotOnTheListCard
import com.emperormoh.myplayground.presentation.componenets.CustomDropDown
import com.emperormoh.myplayground.presentation.componenets.CustomTextField
import com.emperormoh.myplayground.presentation.componenets.SelectBankModal
import com.emperormoh.myplayground.presentation.componenets.SimpleLoaderWithDescription
import com.emperormoh.myplayground.presentation.componenets.VerifiedAccountCard
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransferScreen(
    uiState: LocalTransferUiState,
    onBackClick: () -> Unit,
    onPredictBank: () -> Unit,
    onBankSelected: (Bank) -> Unit,
    onAccountNumberChanged: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onVerifyAccountNumber: () -> Unit
){

   Scaffold (
       //modifier = Modifier.fillMaxSize(),
       topBar = {
           TopAppBar(
               title = {
                   Text(text = "Local Transfer", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
               },
               navigationIcon = {
                   Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                       contentDescription = "back click",
                       modifier = Modifier.clickable { onBackClick.invoke() })
               }
           )
       }
   ) { paddingValues ->
       val keyboard = LocalSoftwareKeyboardController.current
       var accountNumberValue by remember { mutableStateOf("") }

       var showBankModal by remember { mutableStateOf(false) }
       var selectedBank by remember { mutableStateOf<Bank?>(null) }
       Column (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
       ){
           SpaceHeight(12.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Account Details", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(text = "Send to self", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = AlatRed)
            }

          CustomTextField(
              modifier = Modifier.padding(top = 5.dp, start = 12.dp, end = 12.dp),
              placeholder = {
                  Text(text = "Enter name or account number")
              },
              value = accountNumberValue,
              maxLines = 1,
              maxLength = 10,
              keyboardOptions = KeyboardOptions(
                  imeAction = ImeAction.Done, keyboardType = KeyboardType.Text
              ),
              trailingIcon = {
                  if (uiState.isPredictBankLoading) {
                      CircularProgressIndicator(
                          modifier = Modifier.size(15.dp),
                          strokeCap = StrokeCap.Round,
                          color = AlatRed,
                          strokeWidth = 2.dp
                      )
                  }
              },
              onActionClicked = { keyboard?.hide() },
              onTextValueChange = { text ->
                  accountNumberValue = text
                  uiState.destinationAccountNumber = text
                  if (text.length < 10 && text.all{ it.isDigit() }) {
                      onAccountNumberChanged(text)
                  }
                  if (text.all { it.isLetter() }) {
                      onAccountNumberChanged(text)
                  }
                  if (text.length == 5 && text.all { it.isLetter() }) {
                      keyboard?.hide()
                      onPredictBank()
                  }
              },
              errorText = uiState.invalidParameter
          )
           AnimatedVisibility(
               visible = uiState.showPredictedBanks,
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ) {
               Box (modifier = Modifier.padding(horizontal = 12.dp)) {
                   if (uiState.predictedBanks.isNotEmpty() && uiState.showPredictedBanks){
                       Column(
                           modifier = Modifier
                               .shadow(elevation = 1.dp, shape = RoundedCornerShape(10.dp))
                               .background(
                                   color = WhiteTextColor,
                                   shape = RoundedCornerShape(4.dp)
                               )
                               .fillMaxWidth() // Set the maximum height
                               .wrapContentHeight()
                               .padding(10.dp),
                           //contentAlignment = Alignment.Center
                       ){

                           LazyColumn(
                               verticalArrangement = Arrangement.spacedBy(8.dp),
                               modifier = Modifier
                                   .heightIn(max = 200.dp)
                           ) {
                               items(uiState.predictedBanks) { bank ->
                                   BankItemCardOne(
                                       bank = bank,
                                       onClick = {
                                           onBankSelected(bank)
                                           onVerifyAccountNumber()
                                       }
                                   )
                               }
                           }
                           SpaceHeight(18.dp)
                           BankNotOnTheListCard (onClick = { showBankModal = true})
                       }
                   }
               }
           }

           AnimatedVisibility(
               visible = uiState.transferData != null,
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ){
                Column( modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),) {
                    SpaceHeight(20.dp)
                    Text(text = "Bank", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    CustomDropDown(
                        modifier = Modifier.padding(top = 5.dp),
                        value = uiState.transferData?.bank?.bankName ?: "",
                        maxLines = 1,
                        maxLength = 15,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_down),
                                contentDescription = null)
                        },
                        onClick = {
                            showBankModal = true
                        },
                       enabled = false
                    )
                }
           }
           if(showBankModal){
               SelectBankModal(
                   onBankSelected = { bank ->
                       onBankSelected(bank)
                       selectedBank = bank
                       showBankModal = !showBankModal
                       onVerifyAccountNumber()
                   },
                   onBankSearch = {
                       onSearchQueryChanged(it)
                   },
                   onDismiss = {
                       showBankModal = !showBankModal
                   },
                   uiState = uiState,
                   onSearchQueryChanged = {
                       onSearchQueryChanged(it)}
               )
           }
           AnimatedVisibility(
               visible = uiState.isAccountNumberVerificationLoading,
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ){
              SimpleLoaderWithDescription(description = "Verifying account number")
           }
           AnimatedVisibility(
               visible = uiState.isAccountNumberVerified && !uiState.transferData?.accountNumber.isNullOrEmpty() && !uiState.transferData?.nickname.isNullOrEmpty(),
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ){
               uiState.transferData?.nickname?.let { VerifiedAccountCard(nickName = it) }
           }

       }

   }
}

@Composable
fun SpaceHeight(height: Dp = 10.dp){
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun SpaceWidth(width: Dp = 10.dp){
    Spacer(modifier = Modifier.width(width))
}

@Preview(showBackground = true)
@Composable
fun LocalTransferScreenPreview() {
    MyPlayGroundTheme {
        val bank = Bank(
            bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000015.png",
            bankCode = "000015",
            bankName = "ZENITH BANK"
        )
        LocalTransferScreen(onBackClick = {},
            uiState = LocalTransferUiState(
                transferData = TransferData(
                    name = "Adetifa Oluwatosin",
                    accountNumber = "1232232323",
                    bank = bank
                )
            ),
            onPredictBank = {},
            onBankSelected = {},
            onAccountNumberChanged = {},
            onSearchQueryChanged = {},
            onVerifyAccountNumber = {}
        )
    }
}