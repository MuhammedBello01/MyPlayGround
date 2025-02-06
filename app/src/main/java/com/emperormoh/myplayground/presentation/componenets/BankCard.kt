package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.screens.Bank
import com.emperormoh.myplayground.presentation.screens.SpaceWidth
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.BankCardBorder
import com.emperormoh.myplayground.ui.theme.GrayText3
import com.emperormoh.myplayground.ui.theme.LightGray
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MediumLightGray
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.TextFieldBG
import com.emperormoh.myplayground.ui.theme.WhiteTextColor

@Composable
fun BankCard(
    modifier: Modifier = Modifier,
    bank: Bank,
    onClick: (Bank) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick(bank) }
            .background(color = WhiteTextColor, shape = RoundedCornerShape(4.dp))
            .padding(top = 15.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = WhiteTextColor,
                    shape = RoundedCornerShape(4.dp),

                    )
                .border(width = 0.5.dp, color = BankCardBorder, shape = CircleShape)
                .size(50.dp),
            contentAlignment = Alignment.Center
        ) {
            LoadImageFromUrlOpt(
                model = bank.bankLogo,
                contentDescription = bank.bankName,
                placeholderIcon = { Icon(modifier = Modifier.fillMaxSize().padding(4.dp), painter = painterResource(id = R.drawable.ic_bank), contentDescription = null) }
            )
        }
        SpaceWidth(10.dp)
        Text(
            text = bank.bankName,
            style = TextStyle(
                fontFamily = Manrope,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                color = Color.Black,
                letterSpacing = 0.2.sp,

                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun BankNotOnTheListCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Box(modifier.clickable { onClick() }
        .background(color = MediumLightGray, shape = RoundedCornerShape(8.dp))
        .height(46.dp)
        .fillMaxWidth()
        .padding(8.dp)
    ) {
        Row (
            modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Not on the list?",
                style = TextStyle(
                    fontFamily = Manrope,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                    color = Color.Black,
                    //letterSpacing = 0.2.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "View more banks",
                style = TextStyle(
                    fontFamily = Manrope,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                    color = AlatRed,
                    //letterSpacing = 0.2.sp
                ),
                modifier = Modifier.padding(start = 8.dp)
            )
            SpaceWidth(10.dp)
            Image(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "content image",
                modifier = Modifier
                    .padding(end = 4.dp, top = 5.dp, bottom = 4.dp),
                colorFilter = ColorFilter.tint(color = AlatRed),
                contentScale = ContentScale.FillBounds
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
fun BankCardPreview() {
    MyPlayGroundTheme {
//        BankCard(
//            modifier = Modifier,
//            bank = aiBanks.first()
//        ) { }
        BankNotOnTheListCard(modifier = Modifier, onClick = {})
    }
}

val aiBanks = listOf(
    Bank(bankCode = "001", bankLogo = "https://example.com/logo1.png", bankName = "First Bank"),
    Bank(bankCode = "002", bankLogo = "https://example.com/logo2.png", bankName = "Second Bank"),
    Bank(bankCode = "003", bankLogo = "https://example.com/logo3.png", bankName = "Third Bank"),
    Bank(bankCode = "004", bankLogo = null, bankName = "Fourth Bank"),
    Bank(bankCode = "005", bankLogo = "https://example.com/logo5.png", bankName = "Fifth Bank"),
    Bank(bankCode = "006", bankLogo = "https://example.com/logo1.png", bankName = "First Bank"),
    Bank(bankCode = "007", bankLogo = "https://example.com/logo2.png", bankName = "Second Bank"),
    Bank(bankCode = "008", bankLogo = "https://example.com/logo3.png", bankName = "Third Bank"),
    Bank(bankCode = "009", bankLogo = null, bankName = "Fourth Bank"),
    Bank(bankCode = "0010", bankLogo = "https://example.com/logo5.png", bankName = "Fifth Bank")

)