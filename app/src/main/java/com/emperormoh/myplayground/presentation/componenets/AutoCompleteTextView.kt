package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextView() {
    val suggestions = listOf("Apple", "Banana", "Cherry", "Date", "Grapes", "Mango", "Orange", "Pineapple")

    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var expanded by remember { mutableStateOf(false) }

    val filteredSuggestions = suggestions.filter {
        it.contains(textFieldValue.text, ignoreCase = true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = textFieldValue,
                onValueChange = {
                    textFieldValue = it
                    expanded = it.text.isNotEmpty() // Expand only if there's input
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text("Enter fruit name") },
                singleLine = true
            )

            // Dropdown Menu
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            textFieldValue = TextFieldValue(suggestion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AutoCompleteLazyColumn() {
    val allNames = listOf("John", "Jane", "Jack", "Jill", "James", "Jessica", "Jennifer", "Jackson")
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    var filteredNames by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf(false) }

    var filteredBen by remember { mutableStateOf(emptyList<TransferBeneficiary>()) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = textFieldValue,
            onValueChange = {
                textFieldValue = it
                filteredBen = getMockTransferBeneficiaryResponse().beneficiaries
                    .filter { name ->
                        name.destinationAccountNumber.contains(it.text, ignoreCase = true) ||
                                name.nickName.contains(it.text, ignoreCase = true) ||
                                name.destinationAccountName.contains(it.text, ignoreCase = true)}
//                filteredNames = allNames.filter { name ->
//                    name.contains(it.text, ignoreCase = true)
//                }
                expanded = it.text.isNotEmpty() && filteredBen.isNotEmpty()
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter Name") },
            singleLine = true
        )

        if (expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                items(items = filteredBen, key = {it.id}) { ben ->
                    SavedBeneficiaryCard(
                        isShowArrow = false,
                        beneficiary = ben,
                        onBeneficiaryClicked = {
                            textFieldValue = TextFieldValue(ben.destinationAccountNumber)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}