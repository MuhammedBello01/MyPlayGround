package com.emperormoh.myplayground.presentation.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emperormoh.myplayground.presentation.screens.transfer.User
import com.emperormoh.myplayground.ui.theme.AlatRed
import kotlinx.coroutines.launch

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: User,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(16.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(text = "Name: ${user.name}", fontWeight = FontWeight.Bold)
            Text(text = "Account No: ${user.accountNumber}")
            Text(text = "Balance: ${user.accountBalance}")
            Text(
                text = if (user.isActive) "Status: Active" else "Status: Inactive",
                color = if (user.isActive) Color.Green else Color.Red
            )
        }
    }
}

@Composable
fun UserCarousel() {
    val users = remember {
        mutableStateListOf(
            User("Alice Johnson", "123456789", "$5,000", true),
            User("Bob Smith", "987654321", "$1,200", false),
            User("Charlie Adams", "567890123", "$8,300", true)
        )
    }

    val pagerState = rememberPagerState { users.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Carousel
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 8.dp
        ) { page ->
            UserCard(user = users[page], onClick = {})
        }

        Spacer(modifier = Modifier.height(5.dp))

        // **Dotted Indicators**

        PageIndicator(
            modifier = Modifier.width(52.dp),
            pageSize = users.size,
            selectedPage = pagerState.currentPage
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Buttons
        Row {
            Button(
                onClick = {
                    scope.launch {
                        val prevPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                        pagerState.animateScrollToPage(prevPage)
                    }
                },
                enabled = pagerState.currentPage > 0
            ) {
                Text("Previous")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        val nextPage = (pagerState.currentPage + 1).coerceAtMost(users.size - 1)
                        pagerState.animateScrollToPage(nextPage)
                    }
                },
                enabled = pagerState.currentPage < users.size - 1
            ) {
                Text("Next")
            }
        }
    }
}

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageSize: Int,
    selectedPage: Int,
    selectedColor: Color = AlatRed,
    unselectedColor: Color = Color.Gray,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        repeat(times = pageSize) { page ->

            val color = if (page == selectedPage) selectedColor else unselectedColor
            // Animation for scaling
//            val scale by animateFloatAsState(
//                targetValue = if (selectedPage == page) 1.2f else 1.0f, // Scale up active dot
//                animationSpec = tween(durationMillis = 500, easing = LinearEasing) // Smooth animation
//            )

            // Optional: Add a slight alpha change for more effect
//            val alpha by animateFloatAsState(
//                targetValue = if (selectedPage == page) 1.0f else 0.5f,
//                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
//            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    //.scale(scale)
                    .clip(CircleShape)
                    .background(color)
            )
        }

    }

}
