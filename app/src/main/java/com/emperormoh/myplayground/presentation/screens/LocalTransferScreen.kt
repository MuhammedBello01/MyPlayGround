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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.BankItemCardOne
import com.emperormoh.myplayground.presentation.componenets.BankNotOnTheListCard
import com.emperormoh.myplayground.presentation.componenets.CustomButton
import com.emperormoh.myplayground.presentation.componenets.CustomDropDown
import com.emperormoh.myplayground.presentation.componenets.CustomTabs
import com.emperormoh.myplayground.presentation.componenets.CustomTextField
import com.emperormoh.myplayground.presentation.componenets.SelectBankModal
import com.emperormoh.myplayground.presentation.componenets.SimpleLoaderWithDescription
import com.emperormoh.myplayground.presentation.componenets.TabRowItem
import com.emperormoh.myplayground.presentation.componenets.TransferBeneficiary
import com.emperormoh.myplayground.presentation.componenets.VerifiedAccountCard
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.BankCardBorder
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.WhiteTextColor
import kotlinx.coroutines.launch


@Composable
fun LocalTransferRoute(
    viewModel: LocalTransferViewModel,
    onBeneficiarySelected: (TransferBeneficiary) -> Unit,
    onBackClick: () -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    LocalTransferScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onPredictBank = { coroutineScope.launch{
            viewModel.predictBank(uiState.destinationAccountNumber)
        } },
        onBankSelected = { viewModel.setSelectedBank(it) },
        onAccountNumberChanged = { viewModel.resetFieldsUiOnAccountNumberChanged() },
        onSearchQueryChanged = { searchParam ->
            if (searchParam.isNotBlank()) {
                viewModel.searchBanks(query = searchParam)
            } else {
                viewModel.setBanks(viewModel.getAllBanks())
            }
        },
        onVerifyAccountNumber = {
            coroutineScope.launch {
                viewModel.verifyAccountNumber(uiState.destinationAccountNumber)
            }
        },
        getFrequentBeneficiaries = {  viewModel.getFrequentBeneficiaries() },
        getSavedBeneficiaries = { viewModel.getSavedBeneficiaries() },
        onBeneficiarySelected = { onBeneficiarySelected(it)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransferScreen(
    modifier: Modifier = Modifier,
    uiState: LocalTransferUiState,
    onBackClick: () -> Unit,
    onPredictBank: () -> Unit,
    onBankSelected: (Bank) -> Unit,
    onAccountNumberChanged: (String) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onVerifyAccountNumber: () -> Unit,
    getFrequentBeneficiaries: () -> Unit,
    getSavedBeneficiaries: () -> Unit,
    onBeneficiarySelected: (TransferBeneficiary) -> Unit,
){
   Scaffold (
       //modifier = Modifier.fillMaxSize(),
       topBar = {
           TopAppBar(
               title = {
                   Text(text = "Local Transfer", style = TextStyle(
                       fontFamily = Manrope,
                       fontSize = 18.sp,
                       fontWeight = FontWeight.Bold,
                       lineHeight = 21.sp,
                       color = Color.Black,
                       letterSpacing = 0.2.sp),)
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
            .background(color = Color.White)
            //.verticalScroll(rememberScrollState())
       ){
           SpaceHeight(12.dp)
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Account Details",
                    style = TextStyle(
                        fontFamily = Manrope,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 21.sp,
                        color = Color.Black,
                        letterSpacing = 0.2.sp),
                    )
                Text(text = "Send to self",
                    style = TextStyle(
                        fontFamily = Manrope,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 21.sp,
                        color = Color.Black,
                        letterSpacing = 0.2.sp),
                    color = AlatRed)
            }

          CustomTextField(
              modifier = modifier.padding(top = 5.dp, start = 12.dp, end = 12.dp),
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
               visible = uiState.isPredictBankLoading,
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ){
               SimpleLoaderWithDescription(description = "Matching Bank")
           }
           AnimatedVisibility(
               visible = uiState.showPredictedBanks,
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ) {
               Box (modifier = Modifier.padding(horizontal = 12.dp)) {
                   if (uiState.predictedBanks.isNotEmpty() && uiState.showPredictedBanks){
                       Column(
                           modifier = modifier
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
                    Text(text = "Bank",
                        style = TextStyle(
                            fontFamily = Manrope,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 21.sp,
                            color = Color.Black,
                            letterSpacing = 0.2.sp),
                        )
                    CustomDropDown(
                        modifier = modifier.padding(top = 5.dp),
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
            SpaceHeight(20.dp)

           CustomButton(
               modifier = modifier.padding(horizontal = 12.dp, vertical = 20.dp),
               onClick = {},
               enabled = uiState.transferData != null,
               buttonText = "Continue")

           SpaceHeight(15.dp)
           HorizontalDivider(
               modifier = modifier
                   .fillMaxWidth()
                   .padding(horizontal = 1.dp, vertical = 1.dp),
               thickness = 1.dp,
               color = BankCardBorder
           )
           Row(
               modifier = modifier
                   .fillMaxWidth()
                   .padding(horizontal = 14.dp, vertical = 10.dp),
               horizontalArrangement = Arrangement.SpaceBetween,
               verticalAlignment = Alignment.CenterVertically
           ) {
               Text(text = "Beneficiaries",
                   style = TextStyle(
                       fontFamily = Manrope,
                       fontSize = 14.sp,
                       fontWeight = FontWeight.Bold,
                       lineHeight = 21.sp,
                       color = Color.Black,
                       letterSpacing = 0.2.sp),
               )
               Text(text = "ViewAll",
                   style = TextStyle(
                       fontFamily = Manrope,
                       fontSize = 14.sp,
                       fontWeight = FontWeight.Bold,
                       lineHeight = 21.sp,
                       color = Color.Black,
                       letterSpacing = 0.2.sp),
                   color = AlatRed)
           }

           val pagerState = rememberPagerState { 2 }

           val tabItems = listOf(
               TabRowItem(
                   title = { Text(text = "Frequent", style = TextStyle(
                       fontFamily = Manrope,
                       fontSize = 15.sp,
                       fontWeight = FontWeight.Bold,
                       lineHeight = 21.sp,
                       color = Color.Black,
                       letterSpacing = 0.2.sp),) },
                   screen = { FrequentBeneficiariesTab(
                       uiState = uiState,
                       onBeneficiarySelected = onBeneficiarySelected,
                       getFrequentBeneficiaries = getFrequentBeneficiaries
                   )}
               ),
               TabRowItem(
                   title = { Text(text = "Saved", style = TextStyle(
                       fontFamily = Manrope,
                       fontSize = 15.sp,
                       fontWeight = FontWeight.Bold,
                       lineHeight = 21.sp,
                       color = Color.Black,
                       letterSpacing = 0.2.sp)) },
                   screen = { SavedBeneficiariesTab(
                       uiState = uiState,
                       onBeneficiarySelected = onBeneficiarySelected,
                       getSavedBeneficiaries = getSavedBeneficiaries
                   )}
               )
           )
           CustomTabs(tabRowItems = tabItems, pagerState = pagerState)
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
        LocalTransferScreen(
            onBackClick = {},
            uiState = LocalTransferUiState(
                transferData = TransferData(
                    name = "Intifada Somatostatin",
                    accountNumber = "1232232323",
                    bank = bank
                )
            ),
            onPredictBank = {},
            onBankSelected = {},
            onAccountNumberChanged = {},
            onSearchQueryChanged = {},
            onVerifyAccountNumber = {},
            getFrequentBeneficiaries = {},
            getSavedBeneficiaries = {},
            onBeneficiarySelected = {  }
        )
    }
}