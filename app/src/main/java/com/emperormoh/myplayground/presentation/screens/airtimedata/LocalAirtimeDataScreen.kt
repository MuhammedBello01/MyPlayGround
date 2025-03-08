package com.emperormoh.myplayground.presentation.screens.airtimedata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatGeneralText
import com.emperormoh.myplayground.presentation.componenets.CustomTabs
import com.emperormoh.myplayground.presentation.componenets.TabRowItem
import com.emperormoh.myplayground.presentation.componenets.TopBar
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@Composable
fun LocalAirtimeDataRoute(
    modifier: Modifier = Modifier,
    localAirtimeViewModel: LocalAirtimeViewModel,
    onBackClick: () -> Unit,
    onBotClick: () -> Unit
){
    val uiState by localAirtimeViewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        localAirtimeViewModel.initModel(context)
    }
    LocalAirtimeDataScreen(
        modifier = modifier,
        onBackClick = onBackClick,
        onBotClick = onBotClick,
        localAirtimeUiState = uiState,
        onPhoneNumberChanged = localAirtimeViewModel::onPhoneNumberChanged,
        onPredictNetwork = localAirtimeViewModel::predictPhoneNetwork,
        onNetworkSelectionChanged = localAirtimeViewModel::onNetworkSelectionChanged
    )
}
@Composable
fun LocalAirtimeDataScreen(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onBotClick: () -> Unit,
    localAirtimeUiState: LocalAirtimeUiState,
    onPhoneNumberChanged: (String) -> Unit,
    onPredictNetwork: (String) -> Unit,
    onNetworkSelectionChanged: (Int) -> Unit
){

    Scaffold(
        topBar = {
            TopBar(title = "Local Airtime/Data",
                titleFontSize = 15.sp,
                onBack = {onBackClick()},
                onAction = {onBotClick()})
        }
    ) {paddingVal ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingVal)
                //.padding(16.dp)
        ) {
            val pagerState = rememberPagerState { 2 }
            val localAirtimeDataTabItems = listOf(
                TabRowItem(
                    title = {
                        AlatGeneralText(text = "Airtime",
                            fontWeight = 700,
                            fontSize = 12.sp,
                            textColor = if (pagerState.currentPage == 0) colorResource(R.color.CoreUiTextColor) else colorResource(R.color.CoreUiTextFieldHint)
                        )
                   },
                    screen = {
                        LocalAirtimeTab(
                            onPredictNetwork = onPredictNetwork,
                            localAirtimeUiState = localAirtimeUiState,
                            onPhoneNumberChanged = onPhoneNumberChanged,
                            onNetworkSelectionChanged = onNetworkSelectionChanged,
                        )
                        //LocalAirtimeRoute(localAirtimeViewModel!!)
                    }
                ),
                TabRowItem(
                    title = {
                        AlatGeneralText(text = "Data",
                            fontWeight = 700,
                            fontSize = 12.sp,
                            textColor = if (pagerState.currentPage == 1) colorResource(R.color.CoreUiTextColor) else colorResource(R.color.CoreUiTextFieldHint)
                        )
                    },
                    screen = {
                        LocalDataTab()
                    }
                )
            )
            CustomTabs(tabRowItems = localAirtimeDataTabItems, pagerState = pagerState)

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocalAirtimeDataScreenPreview(){
    MyPlayGroundTheme {
        LocalAirtimeDataScreen(
            onBackClick = {},
            onBotClick = {},
            modifier = Modifier,
            localAirtimeUiState = LocalAirtimeUiState(
                isPhoneNumberPredictionLoading = false,
                isPhoneNumberPredicted = true,
                predictedNetworkIndex =  3,
            ),
            onPhoneNumberChanged = { },
            onPredictNetwork = {},
            onNetworkSelectionChanged = {}
        )
    }

}
