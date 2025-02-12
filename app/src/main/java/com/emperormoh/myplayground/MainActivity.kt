package com.emperormoh.myplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emperormoh.myplayground.presentation.componenets.AutoCompleteLazyColumn
import com.emperormoh.myplayground.presentation.componenets.AutoCompleteTextView
import com.emperormoh.myplayground.presentation.screens.LocalTransferRoute
import com.emperormoh.myplayground.presentation.screens.LocalTransferViewModel
import com.emperormoh.myplayground.presentation.screens.SpaceHeight
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPlayGroundTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    val localTransferVm: LocalTransferViewModel = viewModel()
                    LocalTransferRoute(
                        viewModel = localTransferVm,
                        onBeneficiarySelected = {},
                        onBackClick = {}
                    )
//                    Column {
//                        AutoCompleteTextView()
//                        SpaceHeight(30.dp)
//                        AutoCompleteLazyColumn()
//                    }

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