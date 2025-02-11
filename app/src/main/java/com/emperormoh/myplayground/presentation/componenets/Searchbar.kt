package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.MediumLightGray
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@Composable
fun SearchBar(
    onQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit
) {
    var searchParam by remember { mutableStateOf("") }
    Box(modifier = Modifier
        .background(color = MediumLightGray,
            shape = RoundedCornerShape(10.dp))){
        TextField(
            value = searchParam,
            onValueChange = {
                searchParam = it
                onQueryChanged(it) },
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.search_icon),
                contentDescription = "Search") },
            trailingIcon = {
                if (searchParam.isNotEmpty()) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable {
                            searchParam = ""
                            onQueryChanged("") }
                    )
                }
            },
            placeholder = { Text("Search bank name") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors =  TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = AlatRed
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions (
                onSearch = {
                    onSearchClicked()
                }
            ),
        )
    }

}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    MyPlayGroundTheme {
        SearchBar(onSearchClicked = {}, onQueryChanged = {})
    }
}