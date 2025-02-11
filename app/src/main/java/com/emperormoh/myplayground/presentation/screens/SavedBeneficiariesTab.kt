package com.emperormoh.myplayground.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.emperormoh.myplayground.presentation.componenets.SavedBeneficiaryCard
import com.emperormoh.myplayground.presentation.componenets.TransferBeneficiary
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.utils.LifecycleEventHandler

@Composable
fun SavedBeneficiariesTab(
    modifier: Modifier = Modifier,
    uiState: LocalTransferUiState,
    getSavedBeneficiaries: () -> Unit,
    onBeneficiarySelected: (TransferBeneficiary) -> Unit
){

    LifecycleEventHandler(lifecycleEvent = Lifecycle.Event.ON_RESUME) {
        if(uiState.savedBeneficiaries.isEmpty()){
            getSavedBeneficiaries()
        }
    }
    Column(
        modifier = modifier
            .background(color = Color.White)
            .fillMaxSize()
    ){
        SpaceHeight(24.dp)
        if (uiState.savedBeneficiaries.isNotEmpty()){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.savedBeneficiaries) {beneficiary ->
                    SavedBeneficiaryCard (beneficiary = beneficiary) {
                        onBeneficiarySelected(beneficiary)
                    }
                }
            }
        }else {
            Column(
                modifier = modifier.fillMaxWidth().padding(top = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Face,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(50.dp)
                )
                Text(
                    text = "No Frequent Beneficiary Saved",
                    style = TextStyle(
                        fontFamily = Manrope,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 21.sp,
                        color = Color.Black,
                        letterSpacing = 0.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}