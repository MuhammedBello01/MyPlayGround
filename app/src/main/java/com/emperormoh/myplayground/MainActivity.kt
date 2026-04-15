package com.emperormoh.myplayground

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.emperormoh.myplayground.presentation.componenets.audio_play.porcupine.VoiceListenerScreen
import com.emperormoh.myplayground.presentation.screens.transfer.LocalTransferRoute
import com.emperormoh.myplayground.presentation.screens.transfer.LocalTransferViewModel
import com.emperormoh.myplayground.presentation.screens.PaymentHomePage
import com.emperormoh.myplayground.presentation.screens.airtimedata.LocalAirtimeDataRoute
import com.emperormoh.myplayground.presentation.screens.airtimedata.airtime.LocalAirtimeTransactionSummaryScreen
import com.emperormoh.myplayground.presentation.screens.airtimedata.airtime.LocalAirtimeViewModel
import com.emperormoh.myplayground.presentation.screens.airtimedata.data.LocalDataViewModel
import com.emperormoh.myplayground.presentation.screens.camera_stuffs.NumberScannerScreen
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

class MainActivity : FragmentActivity() {
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
                            //AutoCompleteTextField()
//                            Column(modifier = Modifier.fillMaxWidth()) {
//                                SpaceHeight(100.dp)
//
//                                BeneficiaryAutoCompleteTextField(
//                                    beneficiaries = getMockBeneficiaries(),
//                                    onBeneficiarySelected = {}
//                                )
//                                SpaceHeight(10.dp)
//                                AlatRedButton(modifier = Modifier.fillMaxWidth(), text = "Happy", onClick = {})
//                            }
                            //MyScreen()
                            //
                        //BeneficiaryScreen()
//                            val viewModel: SpeechToTextViewModel =  viewModel()
//                            SpeechInputTextField(viewModel)
                            //LottieAnimationView()
                            //MainScreen()
                            //TextToSpeechExample()
                            //PersonList()
                            //BiometricAuthScreen()
                            //TestBioFun()

//                            FinalBiometricAuthScreen(
//                                onAuthSuccess = {
//                                    // Navigate or perform secure action
//                                    Log.d("Auth", "Authenticated successfully!")
//                                },
//                                onAuthError = { error ->
//                                    Log.e("Auth", "Error: $error")
//                                }
//                            )
                            //TopSnackBarTestScreen()
                            //SnackbarDemoScreen()
                        }
                        composable("bill_payment"){
                           // UserCarousel()
                           // AudioPlayerScreen()
                            //VoiceListenerScreen()
                            NumberScannerScreen()
                        }
                        navigation(startDestination = "local_Airtime_data", route = "shared_payment_graph"){
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