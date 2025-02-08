package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.screens.Bank
import com.emperormoh.myplayground.presentation.screens.LocalTransferUiState
import com.emperormoh.myplayground.presentation.screens.SpaceHeight
import com.emperormoh.myplayground.ui.theme.BankGray
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.WhiteTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBankModal(
    onBankSelected: (Bank) -> Unit,
    onBankSearch: (String) -> Unit,
    onDismiss: () -> Unit,
    uiState: LocalTransferUiState,
    onSearchQueryChanged: (String) -> Unit,
){
    var searchQuery by remember { mutableStateOf("") }
    var showBottomSheet by rememberSaveable { mutableStateOf(true) }
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    if (showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                onDismiss()
                },
            containerColor = WhiteTextColor,
            sheetState = modalBottomSheetState
        ) {
            Column(modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Bank",
                        style = TextStyle(
                            fontFamily = Manrope,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 21.sp,
                            color = Color.Black,
                            letterSpacing = 0.2.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        //colorFilter = ColorFilter.tint(AlatRed),
                        modifier = Modifier
                            .size(width = 9.dp, height = 9.26.dp)
                            .clickable {
                                showBottomSheet = false
                                onDismiss()
                            }
                    )
                }
                SpaceHeight(15.dp)
                SearchBar(
                    onQueryChanged = {
                        searchQuery = it
                        onSearchQueryChanged(it)
                    },
                    onSearchClicked = {onBankSearch(searchQuery)})
                SpaceHeight(15.dp)

                BankListSection(title = "Matched Banks",
                    isVisible = uiState.predictedBanks.isNotEmpty(),
                    banks = uiState.predictedBanks,
                    onBankSelected = { onBankSelected(it)})

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = BankGray
                )

                SpaceHeight(15.dp)

                BankListSearchableSection(title = "All Banks",
                    isVisible = uiState.allBanks.isNotEmpty(),
                    banks = uiState.allBanks,
                    onBankSelected = { onBankSelected(it)})
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BankModalPreview() {
    MyPlayGroundTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)){
            Column(modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
                //.verticalScroll(rememberScrollState())
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Bank",
                        style = TextStyle(
                            fontFamily = Manrope,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 21.sp,
                            color = Color.Black,
                            letterSpacing = 0.2.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        //colorFilter = ColorFilter.tint(AlatRed),
                        modifier = Modifier
                            .size(width = 10.dp, height = 10.dp)
                            .clickable {
//                                showBottomSheet = false
//                                onDismiss()
                            }
                    )
                }

                SpaceHeight(15.dp)
                SearchBar(
                    onQueryChanged = {
//                        searchQuery = it
//                        onSearchQueryChanged(it)
                    },
                    onSearchClicked = {
                        //onBankSearch(searchQuery)
                    })
                SpaceHeight(15.dp)

                BankListSection(title = "Matched Bank",
                    banks = aiBanks,
                    isVisible = aiBanks.isNotEmpty(),
                    onBankSelected = {}
                )

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    thickness = 1.dp,
                    color = BankGray
                )
                BankListSection(title = "All Bank",
                    banks = allBanks,
                    isVisible = allBanks.isNotEmpty(),
                    onBankSelected = {}
                )
            }
        }


    }
}