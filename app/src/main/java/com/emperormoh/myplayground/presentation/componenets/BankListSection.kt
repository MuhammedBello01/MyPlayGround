package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.presentation.screens.Bank
import com.emperormoh.myplayground.presentation.screens.SpaceHeight
import com.emperormoh.myplayground.ui.theme.BankGray
import com.emperormoh.myplayground.ui.theme.Error
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.TextFieldHint
import com.emperormoh.myplayground.ui.theme.WhiteTextColor


@Composable
fun BankListSection(
    title: String,
    banks: List<Bank>,
    isVisible: Boolean,
    onBankSelected: (Bank) -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally() + expandVertically(),
        exit = slideOutHorizontally() + shrinkVertically()
    ) {
        Column(modifier = Modifier
            .background(color = WhiteTextColor)
        ) {
            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = title,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontFamily = Manrope,
                    fontWeight = FontWeight.W500,
                    lineHeight = 18.sp,
                    letterSpacing = 0.2.sp
                )
            )
            SpaceHeight(10.dp)
            if (banks.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()

                    //modifier = Modifier.heightIn(max = 200.dp)
                ) {
                    items(banks) { bank ->
                        BankItemCardTwo(
                            bank = bank,
                            onClick = { onBankSelected(bank) }
                        )
                    }
                }
                SpaceHeight(18.dp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BankListSectionPreview() {
    MyPlayGroundTheme {
        BankListSection(title = "AllBanks",
            banks = aiBanks,
            isVisible = aiBanks.isNotEmpty(),
            onBankSelected = {})
    }
}