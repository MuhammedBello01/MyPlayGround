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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.presentation.componenets.BankCard
import com.emperormoh.myplayground.presentation.componenets.BankNotOnTheListCard
import com.emperormoh.myplayground.presentation.componenets.CustomTextField
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.WhiteTextColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransferScreen(
    uiState: LocalTransferUiState,
    onBackClick: () -> Unit,
    onPredictBank: () -> Unit,
    onBankSelected: (Bank) -> Unit,
    onShowAllBanks: () -> Unit,
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
   ) {
       val keyboard = LocalSoftwareKeyboardController.current
       var accountNumberValue by remember { mutableStateOf("") }
       Column (
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
       ){
           SpaceHeight(12.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Account Details", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(text = "Send to self", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = AlatRed)
            }

          CustomTextField(
              modifier = Modifier.padding(top = 10.dp, start = 12.dp, end = 12.dp),
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
                          modifier = Modifier.size(24.dp),
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
                  if (text.length == 5) {
                      keyboard?.hide()
                      onPredictBank()
                  }
              }
          )
           AnimatedVisibility(
               visible = uiState.showPredictedBanks,
               enter = slideInHorizontally() + expandVertically(),
               exit = slideOutHorizontally() + shrinkVertically()
           ) {
               Column(modifier = Modifier.padding(horizontal = 12.dp)) {
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
                                   BankCard(
                                       bank = bank,
                                       onClick = onBankSelected
                                   )
                               }
                           }
                           SpaceHeight(18.dp)
                           BankNotOnTheListCard {
                               onShowAllBanks()
                           }
                       }
                   }


               }
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
        LocalTransferScreen(onBackClick = {},
            uiState = LocalTransferUiState(),
            onPredictBank = {},
            onBankSelected = {},
            onShowAllBanks = {})
    }
}