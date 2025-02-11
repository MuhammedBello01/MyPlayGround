package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.ui.theme.BankGray
import com.emperormoh.myplayground.ui.theme.LightGreenBG
import com.emperormoh.myplayground.ui.theme.LinearIndicatorColor
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import kotlinx.coroutines.launch

@Composable
fun CustomTabLayout(){
    val tabItems = listOf("Tab1", "Tab2", "Tab3")
    val pagerState = rememberPagerState { tabItems.size }
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .padding(5.dp)
                .background(Color.Transparent)
                .clip(RoundedCornerShape(20.dp)),
            indicator = { tabPositions ->
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(
                            tabPositions[pagerState.currentPage]
                        )
                        .width(0.dp)
                        .height(0.dp),
                    color = Color.Transparent
                )
            }
        ){
            tabItems.forEachIndexed { index, title ->
                val color = remember { Animatable(LinearIndicatorColor) }
                LaunchedEffect(pagerState.currentPage == index) {
                    color.animateTo( if (pagerState.currentPage == index)
                        Color.White else BankGray)
                }

                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = if (pagerState.currentPage == index) {
                                TextStyle(
                                    color = LinearIndicatorColor,
                                    fontSize = 18.sp
                                )
                            } else {
                                TextStyle(
                                    color = LinearIndicatorColor,
                                    fontSize = 16.sp
                                )
                            }
                        )
                    },
                    modifier = Modifier.background(
                        color = color.value,
                        shape = RoundedCornerShape(30.dp)
                    )
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(LightGreenBG)
        ) { page ->
            Text(
                text = tabItems[page],
                modifier = Modifier.padding(50.dp),
                color = Color.White
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun PrePreview(){
    MyPlayGroundTheme {
        CustomTabLayout()
    }
}