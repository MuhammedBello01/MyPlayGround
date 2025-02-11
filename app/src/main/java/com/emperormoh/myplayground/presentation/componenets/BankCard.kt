package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.R
import com.emperormoh.myplayground.presentation.screens.Bank
import com.emperormoh.myplayground.presentation.screens.SpaceHeight
import com.emperormoh.myplayground.presentation.screens.SpaceWidth
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.BankCardBorder
import com.emperormoh.myplayground.ui.theme.BankGray
import com.emperormoh.myplayground.ui.theme.Manrope
import com.emperormoh.myplayground.ui.theme.MediumLightGray
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.TextFieldBG
import com.emperormoh.myplayground.ui.theme.WhiteTextColor

@Composable
fun BankItemCardOne(
    modifier: Modifier = Modifier,
    bank: Bank,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
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
                    shape = CircleShape)
                .border(width = 0.5.dp, color = BankCardBorder, shape = CircleShape)
                .size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            LoadImageFromUrlOpt(
                model = bank.bankLogo,
                contentDescription = bank.bankName,
                placeholderIcon = { Icon(modifier = Modifier.fillMaxSize().padding(4.dp), painter = painterResource(id = R.drawable.ic_bank), contentDescription = null) }
            )
        }
        Text(
            text = bank.bankName,
            style = TextStyle(
                fontFamily = Manrope,
                fontSize = 12.sp,
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
        //.wrapContentHeight()
        .fillMaxWidth()
        .padding(3.dp)
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
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    //lineHeight = 21.sp,
                    color = Color.Black,
                    //letterSpacing = 0.2.sp
                ),
                //modifier = Modifier.padding(start = 8.dp)
            )
            SpaceWidth(3.dp)
            Text(
                text = "View more banks",
                style = TextStyle(
                    fontFamily = Manrope,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    //lineHeight = 21.sp,
                    color = AlatRed,
                    //letterSpacing = 0.2.sp
                ),
                //modifier = Modifier.padding(start = 8.dp)
            )
            SpaceWidth(5.dp)
            Image(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = "content image",
                colorFilter = ColorFilter.tint(color = AlatRed),
//                modifier = Modifier
//                    .padding(end = 4.dp, top = 5.dp, bottom = 4.dp),

                //contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun BankItemCardTwo(
    modifier: Modifier = Modifier,
    bank: Bank,
    onClick: () -> Unit
){
    Box(modifier.clickable { onClick() }
        .background(color = BankGray, shape = RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Row(modifier.align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier.background(
                        color = BankGray,
                        shape = CircleShape)
                    .border(width = 0.5.dp, color = BankCardBorder, shape = CircleShape)
                    .size(30.dp).clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                LoadImageFromUrlOpt(
                    model = bank.bankLogo,
                    contentDescription = bank.bankName,
                    placeholderIcon = { Icon(modifier = Modifier.fillMaxSize()
                        .padding(4.dp),
                        painter = painterResource(id = R.drawable.ic_bank),
                        contentDescription = null)
                    }
                )
            }

            Text(
                text = bank.bankName,
                style = TextStyle(
                    fontFamily = Manrope,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 21.sp,
                    color = Color.Black,
                    letterSpacing = 0.2.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp, end = 30.dp)
            )

        }
        Box(modifier = Modifier
            .size(15.dp)
            .border(width = 1.dp, color = BankCardBorder, shape = CircleShape)
            .background(color = WhiteTextColor, shape = CircleShape)
            .align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun VerifiedAccountCard(
    modifier: Modifier = Modifier,
    nickName: String
){
    Row(
        modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(modifier = Modifier
            .padding(4.dp),
            painter = painterResource(id = R.drawable.ic_dummy_eceiver),
            contentDescription = null)

        Text(
            text = nickName,
            style = TextStyle(
                fontFamily = Manrope,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                color = Color.Black,
                letterSpacing = 0.5.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

@Composable
fun SimpleLoaderWithDescription(
    modifier: Modifier = Modifier,
    description: String
){
    Row(
        modifier.fillMaxWidth().padding(start = 14.dp, end = 10.dp, top = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(15.dp),
            strokeCap = StrokeCap.Round,
            color = AlatRed,
            strokeWidth = 2.dp
        )
        SpaceWidth(3.dp)
        Text(
            text = description,
            style = TextStyle(
                fontFamily = Manrope,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 21.sp,
                color = Color.Black,
                letterSpacing = 0.5.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BankCardPreview() {
    MyPlayGroundTheme {
        Column {

            BankItemCardOne(
                modifier = Modifier,
                bank = aiBanks.first()
            ) { }
            SpaceHeight(20.dp)
            BankNotOnTheListCard(modifier = Modifier, onClick = {})
            SpaceHeight(20.dp)

            BankItemCardTwo(bank = aiBanks.first(), onClick = {})
            VerifiedAccountCard(nickName = "Suleiman Muhammed Bello")

            SimpleLoaderWithDescription(description = "Verifying account number")
        }
    }
}

val aiBanks = listOf(
    Bank(bankCode = "000004", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000004.png", bankName = "UNITED BANK FOR AFRICA"),
    Bank(bankCode = "000003", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000003.png", bankName = "FCMB"),
    Bank(bankCode = "000011", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000011.png", bankName = "UNITY BANK"),
    )

val allBanks = listOf(
    Bank(bankCode = "090110", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/090110.png", bankName = "VFD MFB"),
    Bank(bankCode = "000015", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000015.png", bankName = "ZENITH BANK"),
    Bank(bankCode = "000018", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000018.png", bankName = "UNION BANK"),
    Bank(bankCode = "000026", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000026.png", bankName = "TAJ BANK"),
    Bank(bankCode = "000012", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000012.png", bankName = "STANBIC IBTC BANK"),
    Bank(bankCode = "000002", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000002.png", bankName = "KEYSTONE BANK"),
    Bank(bankCode = "000006", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000006.png", bankName = "JAIZ BANK"),
    Bank(bankCode = "000007", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000007.png", bankName = "FIDELITY BANK"),
    Bank(bankCode = "090551", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/090551.png", bankName = "FairMoney MFB"),
    Bank(bankCode = "090267", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/090267.png", bankName = "KUDA MICROFINANCE BANK"),
    Bank(bankCode = "000003", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000003.png", bankName = "FCMB"),
    Bank(bankCode = "000004", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000004.png", bankName = "UNITED BANK FOR AFRICA"),
    Bank(bankCode = "000011", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000011.png", bankName = "UNITY BANK"),
    Bank(bankCode = "035", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/035.png", bankName = "ALATbyWEMA"),
    Bank(bankCode = "035", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/035.png", bankName = "WEMA BANK"),
    Bank(bankCode = "000013", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000013.png", bankName = "GUARANTY TRUST BANK"),
    Bank(bankCode = "000010", bankLogo = "https://wemaalatblobstorage.blob.core.windows.net/bankimages/000010.png", bankName = "ECOBANK"),
    )