package com.emperormoh.myplayground.presentation.componenets.alat_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.screens.common.CurrencyAmountInputVisualTransformation
import com.emperormoh.myplayground.presentation.theme.suisseIntlFamily
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import kotlin.math.roundToLong

@Composable
fun AlatGeneralText(
    modifier: Modifier = Modifier,
    text:String,
    fontSize: TextUnit = 12.sp,
    textColor: Color = colorResource(R.color.CoreUiTextColor),
    fontWeight:Int = 400, //light = 200, normal = 400, medium = 500, bold =700
    textAlign: TextAlign = TextAlign.Start
){
    Text(
        modifier = modifier,
        text = text,
        fontSize = fontSize,
        fontFamily = suisseIntlFamily,
        color = textColor,
        fontWeight = FontWeight(fontWeight),
        textAlign = textAlign
    )
}

@Composable
fun AlatEditTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true,
    hasError: Boolean = false,
    maxLines:Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    errorMessage: String? = null,
    fontSize: TextUnit = 14.sp,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefixText:String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val textColor = if(hasError) {
        colorResource(R.color.CoreUiErrorTextColor)
    }
    else{
        colorResource(R.color.CoreUiTextColor)
    }

    val background = if (value.isEmpty() && !hasError){
        colorResource(R.color.CoreUiSurfaceBackground)
    }else if(hasError){
        colorResource(R.color.CoreUiErrorBackground)
    }else if(value.isNotEmpty()){
        colorResource(R.color.CoreUiBackgroundColor)
    }
    else{
        colorResource(R.color.CoreUiBackgroundColor)
    }

    val backgroundBorderColor = if(hasError) {
        colorResource(R.color.CoreUiPinkBorder)
    }
    else{
        colorResource(R.color.CoreUiBorderColor)
    }


    Column(modifier = modifier) {
        AlatGeneralText(
            text = label,
            fontWeight = 500
        )
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .background(
                    color = background,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = backgroundBorderColor
                )
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions.copy(
                    keyboardType = if (isPassword) KeyboardType.Password else keyboardOptions.keyboardType
                ),
                textStyle = TextStyle(
                    fontFamily = suisseIntlFamily,
                    color = textColor,
                    fontSize = fontSize
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = textColor
                ),
                enabled = enabled,
                maxLines = maxLines,
                prefix = {
                    AlatGeneralText(
                        text = prefixText ?: "",
                        fontSize = fontSize,
                        textColor = textColor
                    )
                },
                trailingIcon = {
                    if (isPassword) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    } else {
                        trailingIcon?.invoke()
                    }
                },
                leadingIcon = leadingIcon,
                placeholder = { AlatGeneralText(
                    text = placeholder,
                    textColor = colorResource(R.color.CoreUiTextFieldHint)
                ) },
                visualTransformation = visualTransformation
                    ?: if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
            )
        }

        if (hasError && errorMessage != null) {
            AlatGeneralText(
                text = errorMessage,
                textColor = colorResource(R.color.CoreUiErrorTextColor),
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun AlatEditTextFieldNoLabel(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true,
    hasError: Boolean = false,
    maxLines:Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    errorMessage: String? = null,
    fontSize: TextUnit = 14.sp,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefixText:String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val textColor = if(hasError) {
        colorResource(R.color.CoreUiErrorTextColor)
    }
    else{
        colorResource(R.color.CoreUiTextColor)
    }

    val background = if (value.isEmpty() && !hasError){
        colorResource(R.color.CoreUiSurfaceBackground)
    }else if(hasError){
        colorResource(R.color.CoreUiErrorBackground)
    }else if(value.isNotEmpty()){
        colorResource(R.color.CoreUiBackgroundColor)
    }
    else{
        colorResource(R.color.CoreUiBackgroundColor)
    }

    val backgroundBorderColor = if(hasError) {
        colorResource(R.color.CoreUiPinkBorder)
    }
    else{
        colorResource(R.color.CoreUiBorderColor)
    }


    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .background(
                    color = background,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = backgroundBorderColor
                )
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions.copy(
                    keyboardType = if (isPassword) KeyboardType.Password else keyboardOptions.keyboardType
                ),
                textStyle = TextStyle(
                    fontFamily = suisseIntlFamily,
                    color = textColor,
                    fontSize = fontSize
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = textColor
                ),
                enabled = enabled,
                maxLines = maxLines,
                prefix = {
                    AlatGeneralText(
                        text = prefixText ?: "",
                        fontSize = fontSize,
                        textColor = textColor
                    )
                },
                trailingIcon = {
                    if (isPassword) {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    } else {
                        trailingIcon?.invoke()
                    }
                },
                leadingIcon = leadingIcon,
                placeholder = { AlatGeneralText(
                    text = placeholder,
                    textColor = colorResource(R.color.CoreUiTextFieldHint)
                ) },
                visualTransformation = visualTransformation
                    ?: if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
            )
        }

        if (hasError && errorMessage != null) {
            AlatGeneralText(
                text = errorMessage,
                textColor = colorResource(R.color.CoreUiErrorTextColor),
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun AlatAmountTextFieldNoLabel(
    amount: Double,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    maxLength: Int = 12,
    fontSize: TextUnit = 16.sp,
    enabled: Boolean = true,
    currencySymbol: String = "",
    hasError: Boolean = false,
    errorText: String? = null,
    placeholder: String = "",
    onAmountChange: (Double) -> Unit
) {
    val pattern = remember { Regex("^\\d*\\.?\\d{0,2}$") }
    val displayAmount = if (amount > 0.0) (amount * 100).roundToLong().toString() else ""

    AlatEditTextFieldNoLabel(
        modifier = modifier,
        value = displayAmount,
        maxLength = maxLength,
        maxLines = maxLines,
        onValueChange = { newText: String ->
            if (newText.isEmpty() || newText.matches(pattern) /*&& !newText.contains(".")*/) {
                val cleanedText = newText.replace(",", "").replace(".", "")
                onAmountChange(
                    if (cleanedText.isNotEmpty()) {
                        cleanedText.toDouble() / 100.0
                    } else 0.0
                )
            }

        },
        fontSize = fontSize,
        enabled = enabled,
        hasError = hasError,
        errorMessage = errorText,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        visualTransformation = CurrencyAmountInputVisualTransformation(
            currencySymbol = "₦",
            fixedCursorAtTheEnd = true
        ),
        prefixText = currencySymbol,
        placeholder = placeholder
    )
}

@Composable
fun PinTextFieldTwo(
    modifier: Modifier = Modifier,
    size: Int = 4,
    value: String,
    hasError: Boolean = false,
    errorMessage: String? = null,
    onValueChange: (String) -> Unit
) {
    val textColor = if(hasError) {
        colorResource(R.color.CoreUiErrorTextColor)
    }
    else{
        colorResource(R.color.CoreUiTextColor)
    }

    val background = if (value.isEmpty() && !hasError){
        colorResource(R.color.CoreUiSurfaceBackground)
    }else if(hasError){
        colorResource(R.color.CoreUiErrorBackground)
    }else if(value.isNotEmpty()){
        colorResource(R.color.CoreUiSurfaceBackground)
        //colorResource(R.color.CoreUiBackgroundColor)
    }
    else{
        colorResource(R.color.CoreUiBackgroundColor)
    }

    val backgroundBorderColor = if(hasError) {
        colorResource(R.color.CoreUiPinkBorder)
    }
    else{
        Color.Transparent
        //colorResource(R.color.CoreUiBorderColor)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth(),
            enabled = true,
            value = value,
            onValueChange = {
                if (it.length <= size && it.isDigitsOnly()) {
                    onValueChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            decorationBox = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(size) { index ->
                        val char = when {
                            index >= value.length -> ""
                            else -> value[index].toString()
                        }
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(48.dp)
                                .height(56.dp)
                                .background(shape = RoundedCornerShape(10.44.dp), color = background)
                                .border(
                                    width = 0.87.dp,
                                    color = backgroundBorderColor,
                                    shape = RoundedCornerShape(10.44.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            AlatGeneralText(
                                modifier = Modifier
                                    .width(47.74.dp)
                                    .padding(2.dp),
                                //text = if (char.isNotBlank()) "\u2022" else "",
                                text = if (char.isNotBlank()) "*" else "-",
                                fontWeight = 500,
                                textColor = textColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

        )
        if (hasError && errorMessage != null) {
            AlatGeneralText(
                text = errorMessage,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AlatOpenDropDownUi(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    fontSize: TextUnit = 14.sp,
    maxLines:Int = 1,
    enabled: Boolean = false,
    onValueChange: (String) -> Unit,
    textColor: Color = colorResource(R.color.CoreUiTextColor),
    background: Color = colorResource(R.color.CoreUiSurfaceBackground),
    onClick: () -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth()
            .background(
                color = background,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(8.dp),
                color = background
            )
            .padding(vertical = 10.dp)
            .clickable {
                if(enabled){
                    onClick()
                } },
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            textStyle = TextStyle(
                fontFamily = suisseIntlFamily,
                color = textColor,
                fontSize = fontSize
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.CoreUiSurfaceBackground),
                disabledContainerColor = colorResource(R.color.CoreUiSurfaceBackground),
                unfocusedContainerColor = colorResource(R.color.CoreUiSurfaceBackground),
                disabledIndicatorColor = colorResource(R.color.CoreUiSurfaceBackground),
                unfocusedIndicatorColor = colorResource(R.color.CoreUiSurfaceBackground),
                focusedIndicatorColor = colorResource(R.color.CoreUiSurfaceBackground),
                cursorColor = textColor
            ),
            enabled = enabled,
            maxLines = maxLines,
            trailingIcon = {
                Image(
                    painter = painterResource(R.drawable.core_ui_arrow_down),
                    contentDescription = null,
                    colorFilter = null,
                    modifier = Modifier
                        .size(28.dp)
                )
            },
            placeholder = { AlatGeneralText(
                text = placeholder,
                textColor = colorResource(R.color.CoreUiTextFieldHint)
            ) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InputsPreview() {
    MyPlayGroundTheme {
        Column(
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            AlatEditTextField(value = "", onValueChange = {}, label = "Label", placeholder = "Enter input")
            Spacer(Modifier.height(10.dp))
            AlatEditTextFieldNoLabel(value = "", onValueChange = {}, placeholder = "Enter input")
            Spacer(Modifier.height(10.dp))
            PinTextFieldTwo(onValueChange = {}, value = "7869")
            Spacer(Modifier.height(10.dp))
            AlatOpenDropDownUi(value = "", onValueChange = {},
                placeholder = "Select data bundle",
                onClick = {})
            Spacer(Modifier.height(10.dp))
            var amount by remember {
                mutableDoubleStateOf(0.0)
            }
            AlatAmountTextFieldNoLabel(
                amount = amount,
                onAmountChange = {
                    amount = it
                },
                placeholder = "₦0.00"
            )
        }



    }
}