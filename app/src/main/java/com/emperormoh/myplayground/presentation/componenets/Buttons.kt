package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.GrayIndicator
import com.emperormoh.myplayground.ui.theme.GrayText2
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.WhiteTextColor

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = false,
    buttonText: String = "Next",
    buttonHeight: Dp = 45.dp
){
    Button(
        modifier = modifier.fillMaxWidth()
            .height(buttonHeight),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AlatRed,
            contentColor = WhiteTextColor,
            disabledContainerColor = GrayIndicator,
            disabledContentColor = AlatRed),
    ) {
        Text(
            text = buttonText,
            style = TextStyle(
                fontFamily = Manrope,
                fontWeight = FontWeight.W700,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.2.sp,
                textAlign = TextAlign.Center),
            color = if (enabled) Color.White else GrayText2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview(){
    MyPlayGroundTheme {
        CustomButton(onClick = {}, enabled = true)
    }
}