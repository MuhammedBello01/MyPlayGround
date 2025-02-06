package com.emperormoh.myplayground

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emperormoh.myplayground.presentation.screens.LocalTransferScreen
import com.emperormoh.myplayground.presentation.screens.LocalTransferUiState
import com.emperormoh.myplayground.presentation.screens.LocalTransferViewModel
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPlayGroundTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    val localTransferVm: LocalTransferViewModel = viewModel()
                    val localTransferUiState by localTransferVm.uiState.collectAsStateWithLifecycle()
                    val context = LocalContext.current
                    val coroutineScope = rememberCoroutineScope()
                    LocalTransferScreen(
                        onBackClick = {},
                        uiState = localTransferUiState,
                        onPredictBank = {
                            coroutineScope.launch{localTransferVm.predictBank(localTransferUiState.destinationAccountNumber)} },
                        onBankSelected = {
                            localTransferVm.dismissPredictionColumn()
                        },
                        onShowAllBanks = { Toast.makeText(context, "Hello from Compose!", Toast.LENGTH_SHORT).show()}
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPlayGroundTheme {
        Greeting("Android")
    }
}