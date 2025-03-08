package com.emperormoh.myplayground.presentation.componenets.alat_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme

@Composable
fun AlatRedButton(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: Int = 650,
    onClick: () -> Unit,
    isEnabled: Boolean = true
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) colorResource(R.color.CoreUiAlatRed) else colorResource(R.color.CoreUiSurfaceBackground)
        ),
        //border = BorderStroke(0.5.dp, colorResource(R.color.CoreUiPinkBorder)),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        AlatGeneralText(
            text = text,
            textColor = if (isEnabled) colorResource(R.color.CoreUiAlwaysWhite) else colorResource(R.color.CoreUiTextFieldHint),
            fontWeight = fontWeight
        )
    }
}

@Composable
fun AlatWhiteRedTextButton(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: Int = 650,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.CoreUiBackgroundColor)
        ),
        border = BorderStroke(0.5.dp, colorResource(R.color.CoreUiAlatRed)),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        AlatGeneralText(
            text = text,
            textColor = colorResource(R.color.CoreUiAlatRed),
            fontWeight = fontWeight
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlatButtonsPreview(){

    MyPlayGroundTheme {
        Column {
            AlatRedButton(modifier = Modifier.fillMaxWidth(), text = "Next", onClick = {}, isEnabled = true)
            Spacer(Modifier.height(20.dp))
            AlatWhiteRedTextButton(modifier = Modifier.fillMaxWidth(), text = "Next", onClick = {})

        }
    }
}