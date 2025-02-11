package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.Error
import com.emperormoh.myplayground.ui.theme.GrayIndicator
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.TextColor
import com.emperormoh.myplayground.ui.theme.TextFieldBorder
import com.emperormoh.myplayground.ui.theme.TextFieldHint
import com.emperormoh.myplayground.ui.theme.TextFieldStyle
import com.emperormoh.myplayground.ui.theme.WhiteTextColor

@Composable
fun CustomTextField(
    value: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    maxLength: Int = Integer.MAX_VALUE,
    textColor: Color = TextColor,
    fontSize: TextUnit = 15.sp,
    enabled: Boolean = true,
    hasInfoText: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    onActionClicked: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextValueChange: (String) -> Unit
){
    Column (
        modifier = Modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.Top
    ){
        Box(
            modifier.background(color = WhiteTextColor, shape = RoundedCornerShape(10.dp))
                .border(width = 1.dp, shape = RoundedCornerShape(10.dp), color = TextFieldBorder)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center

        ){
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = value,
                onValueChange = {
                    if (it.length <= maxLength) {
                        onTextValueChange(it)
                    }
                },
                keyboardActions = KeyboardActions(
                    onNext = { onActionClicked() },
                    onDone = { onActionClicked() }),
                keyboardOptions = keyboardOptions,
                textStyle = TextFieldStyle + TextStyle(
                    color = textColor,
                    fontSize = fontSize,
                ),
                label = null,
                prefix = {
                    Text(
                        text = prefixText.orEmpty(),
                        style = TextStyle(
                            color = GrayIndicator,
                            fontSize = fontSize,
                            fontFamily = Manrope,
                            fontWeight = FontWeight.W400,
                            lineHeight = 24.sp
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = AlatRed
                ),
                maxLines = maxLines,
                enabled = enabled,
                trailingIcon = trailingIcon,
                leadingIcon = leadingIcon,
                placeholder = placeholder,
                visualTransformation = visualTransformation
            )
        }
        AnimatedVisibility(errorText?.isEmpty() == false) {
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = if (hasInfoText) painterResource(id = R.drawable.idea) else painterResource(
                        id = R.drawable.ic_error_vector
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = errorText.orEmpty(),
                    style = TextStyle(
                        color = if (hasInfoText) TextFieldHint else Error,
                        fontSize = 12.sp,
                        fontFamily = Manrope,
                        fontWeight = FontWeight.W500,
                        lineHeight = 18.sp,
                        letterSpacing = 0.2.sp
                    )
                )
            }
        }
    }
}

@Composable
fun CustomDropDown(
    value: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    maxLength: Int = Integer.MAX_VALUE,
    textColor: Color = TextColor,
    fontSize: TextUnit = 16.sp,
    enabled: Boolean = true,
    hasInfoText: Boolean = false,
    errorText: String? = null,
    prefixText: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
){
    CustomTextField(
        modifier = modifier.clickable {
            onClick()
        },
        value = value,
        maxLines = maxLines,
        maxLength = maxLength,
        onTextValueChange = {},
        prefixText = prefixText,
        textColor = textColor,
        fontSize = fontSize,
        enabled = enabled,
        hasInfoText = hasInfoText,
        errorText = errorText,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        onActionClicked = onClick

    )
}

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }
@Composable
fun PreviewWrapper(content: @Composable () -> Unit) {
    CompositionLocalProvider(

        value = LocalNavController provides rememberNavController(),
        content = content
    )
}
@Preview(showBackground = true)
@Composable
fun LocalTransferScreenPreview() {
    PreviewWrapper {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ){
          CustomTextField(value = "", placeholder = { Text(text = "Enter name or account number") }) {}
            CustomDropDown(value = "ZenithBanks", trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_down),
                    contentDescription = null)
            },)
        }

    }
}