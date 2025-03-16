package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Popup
import com.emperormoh.myplayground.presentation.componenets.alat_components.AlatRedButton
import com.emperormoh.myplayground.presentation.screens.transfer.SpaceHeight

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField() {
    Column {
        var text by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        val options = listOf("Apple", "Banana", "Cherry", "Date", "Grapes", "Mango", "Orange")
        val filteredOptions = options.filter { it.contains(text, ignoreCase = true) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    expanded = filteredOptions.isNotEmpty()
                },
                modifier = Modifier.menuAnchor(),
                label = { Text("Enter fruit") },
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            text = option
                            expanded = false
                        }
                    )
                }
            }
        }
        SpaceHeight(10.dp)
        AlatRedButton(modifier = Modifier.fillMaxWidth(), text = "Happy", onClick = {})
    }

}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BeneficiaryAutoCompleteTextField(
//    beneficiaries: List<TransferBeneficiary>,
//    onBeneficiarySelected: (TransferBeneficiary) -> Unit
//) {
//
//    var text by remember { mutableStateOf("") }
//    var expanded by remember { mutableStateOf(false) }
//
//    val filteredBeneficiaries = if (text.length >= 2) {
//        beneficiaries.filter {
//            it.destinationAccountNumber.contains(text, ignoreCase = true)
//        }
//    } else {
//        emptyList()
//    }
//
//    ExposedDropdownMenuBox(
//        modifier = Modifier.fillMaxWidth(),
//        expanded = expanded,
//        onExpandedChange = { expanded = it && text.length >= 2 }
//    ) {
//        TextField(
//            value = text,
//            onValueChange = {
//                text = it
//                expanded = it.length >= 2 && filteredBeneficiaries.isNotEmpty()
//            },
//            modifier = Modifier.fillMaxWidth().menuAnchor(),
//            label = { Text("Select Beneficiary") },
//            trailingIcon = {
//                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
//            }
//        )
//
//        ExposedDropdownMenu(
//            //modifier = Modifier.fillMaxWidth(),
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            filteredBeneficiaries.forEach { beneficiary ->
//                DropdownMenuItem(
//                    text = {
//                        SavedBeneficiaryCard(
//                            //modifier = Modifier.fillMaxWidth(),
//                            beneficiary = beneficiary,
//                            isShowArrow = false,
//                            onBeneficiaryClicked = {
//                                text = beneficiary.destinationAccountNumber
//                                expanded = false
//                                onBeneficiarySelected(beneficiary)
//                            }
//                        )
//                    },
//                    //modifier = Modifier.fillMaxWidth(),
//                    onClick = { } // onClick handled inside `SavedBeneficiaryCard`
//                )
//            }
//        }
//    }
//}

@Composable
fun BeneficiaryAutoCompleteTextFieldLazy(
    beneficiaries: List<TransferBeneficiary>,
    onBeneficiarySelected: (TransferBeneficiary) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val filteredBeneficiaries = if (text.length >= 2) {
        beneficiaries.filter {
            it.destinationAccountName.contains(text, ignoreCase = true) ||
                    it.destinationAccountNumber.contains(text, ignoreCase = true)
        }
    } else {
        emptyList()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                expanded = it.length >= 2 && filteredBeneficiaries.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            label = { Text("Select Beneficiary") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        )

        if (expanded) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp)
            ) {
                items(filteredBeneficiaries) { beneficiary ->
                    SavedBeneficiaryCard(
                        modifier = Modifier
                            .padding(8.dp),
                        beneficiary = beneficiary,
                        isShowArrow = false,
                        onBeneficiaryClicked = {
                            text = beneficiary.destinationAccountNumber
                            expanded = false
                            onBeneficiarySelected(beneficiary)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BeneficiaryAutoCompleteTextField(
    beneficiaries: List<TransferBeneficiary>,
    onBeneficiarySelected: (TransferBeneficiary) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val textFieldSize = remember { mutableStateOf(Size.Zero) }

    val filteredBeneficiaries = if (text.length >= 2) {
        beneficiaries.filter {
            it.destinationAccountName.contains(text, ignoreCase = true) ||
                    it.destinationAccountNumber.contains(text, ignoreCase = true)
        }
    } else emptyList()

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                expanded = it.length >= 2 && filteredBeneficiaries.isNotEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize.value = coordinates.size.toSize() // Capture text field size
                },
            label = { Text("Select Beneficiary") },
            trailingIcon = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true
        )

        if (expanded) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, textFieldSize.value.height.toInt()), // Position dropdown below TextField
                onDismissRequest = { expanded = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp) // Prevents too large dropdowns
                    ) {
                        items(filteredBeneficiaries) { beneficiary ->
                            SavedBeneficiaryCard(
                                modifier = Modifier
                                    .clickable {
                                        text = beneficiary.destinationAccountName
                                        expanded = false
                                        onBeneficiarySelected(beneficiary)
                                    },
                                beneficiary = beneficiary,
                                isShowArrow = false,
                                onBeneficiaryClicked = {}
                            )
                        }
                    }
                }
            }
        }
    }
}



