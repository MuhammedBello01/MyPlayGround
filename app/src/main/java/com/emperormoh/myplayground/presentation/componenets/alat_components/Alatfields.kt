package com.emperormoh.myplayground.presentation.componenets.alat_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.theme.suisseIntlFamily

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