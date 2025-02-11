package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.ui.theme.AlatRed
import com.emperormoh.myplayground.ui.theme.BankGray
import com.emperormoh.myplayground.ui.theme.MyPlayGroundTheme
import com.emperormoh.myplayground.ui.theme.WhiteTextColor
import kotlinx.coroutines.launch


val  tabRowItems = listOf(
    TabRowItem(
        enabled = false, title = {Text(text = "Frequent")},
        screen = { TODO() }
    ),
    TabRowItem(
        enabled = false, title = {Text(text = "Saved")},
        screen = { TODO() }
    ),

)

data class TabRowItem(
    val enabled: Boolean = true,
    val title: @Composable () -> Unit,
    val screen: @Composable () -> Unit,
)

@Composable
fun CustomTab(
    modifier: Modifier = Modifier,
    item: TabRowItem,
    selected: Boolean,
    enabled: Boolean = true,
){
    Tab(
        selected = selected,
        onClick = {},
        enabled = enabled
    ) {
        Box(
            modifier = modifier
                .background(color = if (selected) Color.White else BankGray,
                    shape = if (selected) RoundedCornerShape(8.dp) else RectangleShape)
                .shadow(
                    elevation = if (selected) 2.dp else 0.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = Color.White,
                    spotColor = Color.White
                )
                .height(38.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            item.title()
        }
    }
}

@Composable
fun CustomTabs(
    modifier: Modifier = Modifier,
    tabRowItems: List<TabRowItem>,
    pagerState: PagerState = rememberPagerState { tabRowItems.size },
){
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(Color.White)
    ){
        Box(
            modifier = modifier.background(color = BankGray,
                shape = RoundedCornerShape(10.dp))
                .padding(vertical = 0.5.dp)
                .border(
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = BankGray
                    )
                )
        ){
            TabRow(
                modifier = modifier
                    .background(color = BankGray, shape = RoundedCornerShape(8.dp))
                    .padding(1.dp),
                selectedTabIndex = pagerState.currentPage,
                indicator = {},
                divider = {}
            ){
                tabRowItems.forEachIndexed { index, item ->
                    CustomTab(
                        enabled = item.enabled,
                        modifier = modifier
                            .clickable(
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null,
                                onClick = {
                                    scope.launch {
                                        pagerState.scrollToPage(index)
                                    }
                                }
                            )
                            .testTag("tab-${index + 1}")
                            .semantics {
                                contentDescription = "Tab"
                            },
                        item = item,
                        selected = (pagerState.currentPage == index)
                    )
                }
            }
        }
        HorizontalPager(state = pagerState) { page ->
            tabRowItems[page].screen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TabPreview(){
    MyPlayGroundTheme {
        val items = listOf(
            TabRowItem(
                true,
                { Text("Completed") },
                {}),
            TabRowItem(
                true,
                { Text("Ongoing") },
                {})
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CustomTabs(tabRowItems = items)
        }
    }
}