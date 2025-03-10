package com.emperormoh.myplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emperormoh.myplayground.presentation.componenets.UserCarousel
import com.emperormoh.myplayground.presentation.screens.transfer.LocalTransferRoute
import com.emperormoh.myplayground.presentation.screens.transfer.LocalTransferViewModel
import com.emperormoh.myplayground.presentation.screens.PaymentHomePage
import com.emperormoh.myplayground.presentation.screens.airtimedata.LocalAirtimeDataRoute
import com.emperormoh.myplayground.presentation.screens.airtimedata.airtime.LocalAirtimeTransactionSummaryScreen
import com.emperormoh.myplayground.presentation.screens.airtimedata.airtime.LocalAirtimeViewModel
import com.emperormoh.myplayground.presentation.screens.airtimedata.data.LocalDataViewModel
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

class MainActivity : ComponentActivity() {
    private var navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPlayGroundTheme {
                navController = rememberNavController()
                Surface (modifier = Modifier.fillMaxSize()) {
                    val localTransferVm: LocalTransferViewModel = viewModel()
//                    LocalTransferRoute(
//                        viewModel = localTransferVm,
//                        onBeneficiarySelected = {},
//                        onBackClick = {}
//                    )
//                    Column {
//                        AutoCompleteTextView()
//                        SpaceHeight(30.dp)
//                        AutoCompleteLazyColumn()
//                    }

                    //UserCarousel()
                    //SelectOneFromLazyColumn()
                    //SelectMultipleFromLazyColumn()
                    //StepIndicatorScreen()
                    //VerticalStepIndicatorScreen()
                    //SecureDataScreen(this)
                    NavHost(
                        navController = navController!!,
                        startDestination = "Landing_page",
                    ){
                        composable("Landing_page"){
                            PaymentHomePage(onOptionClick = {
                                navController!!.navigate(it)
                            })
                        }
                        composable("send_money"){
                            //SelectOneFromLazyColumn()
                            LocalTransferRoute(
                            viewModel = localTransferVm,
                            onBeneficiarySelected = {},
                            onBackClick = {})
                        }
                        composable("bill_payment"){
                            UserCarousel()
                        }
                        composable("local_Airtime_data"){

                            //VerticalStepIndicatorScreen()
                            //ParentComposable()
                            val localAirtimeViewModel: LocalAirtimeViewModel = viewModel()
                            val localDataViewModel: LocalDataViewModel = viewModel()
//                            LocalAirtimeTab(onPredictNetwork = {
//
//                                localAirtimeViewModel.InitModel(this@MainActivity)
//                                localAirtimeViewModel.predictPhoneNetwork(it)
//                            })
                            LocalAirtimeDataRoute(
                                localAirtimeViewModel = localAirtimeViewModel,
                                onBackClick = { navController!!.popBackStack() },
                                onBotClick = {  },
                                localDataViewModel = localDataViewModel,
                                onNavigateToAirtimeAmountRoute = { navController!!.navigate(it)},
                                onNavigateToDataAmountRoute = {}
                            )
                        }
                        composable("transaction_summary_screen") {

                            LocalAirtimeTransactionSummaryScreen(
                                onBackClick = { navController!!.popBackStack()},
                                onBotClick = {},
                                onPinChange = {},
                                onPayClick = {}
                            )
                        }

                    }


                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPlayGroundTheme {

    }
}