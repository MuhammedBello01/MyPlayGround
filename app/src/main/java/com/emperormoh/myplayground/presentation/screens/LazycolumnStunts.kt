package com.emperormoh.myplayground.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emperormoh.myplayground.presentation.screens.transfer.SpaceHeight
import com.emperormoh.myplayground.presentation.screens.transfer.User

val mockUsers = listOf(
    User("Alice Johnson", "1234567890", "$5,320.75", true),
    User("Bob Smith", "2345678901", "$1,250.00", false),
    User("Charlie Brown", "3456789012", "$8,910.40", true),
    User("Diana Prince", "4567890123", "$15,600.99", false),
    User("Ethan Hunt", "5678901234", "$3,420.15", true),
    User("Fiona Gallagher", "6789012345", "$12,750.60", true),
    User("George Costanza", "7890123456", "$290.50", false),
    User("Hannah Montana", "8901234567", "$9,100.20", true),
    User("Isaac Newton", "9012345678", "$7,550.00", false),
    User("Jessica Pearson", "0123456789", "$25,840.33", true),
    User("Kevin Malone", "1357924680", "$910.75", false),
    User("Luna Lovegood", "2468013579", "$18,320.10", true),
    User("Michael Scott", "9876543210", "$4,600.55", true),
    User("Nancy Wheeler", "8765432109", "$2,750.25", false),
    User("Oliver Queen", "7654321098", "$13,920.78", true),
    User("Pam Beesly", "6543210987", "$6,300.00", false),
    User("Quentin Tarantino", "5432109876", "$11,505.80", true),
    User("Rachel Green", "4321098765", "$5,820.90", true),
    User("Steve Rogers", "3210987654", "$14,000.00", false),
    User("Tony Stark", "2109876543", "$99,999.99", true)
)

@Composable
fun SelectOneFromLazyColumn() {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var selectedUser by remember { mutableStateOf<User?>(null) }

    Column {
        SpaceHeight(100.dp)
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            //this method can be used too
//            itemsIndexed(mockUsers) { index, user ->
//                PersonCard(
//                    user = user,
//                    isSelected = (selectedIndex == index),
//                    onClick = {
//                        selectedIndex = index
//                        selectedUser = mockUsers[index]
//                    }
//                )
//            }
            items(mockUsers) { user ->
                PersonCard(
                    user = user,
                    isSelected = (selectedUser == user),
                    onClick = { selectedUser = user }
                )
            }
        }

        // Display selected user details below the list
        selectedUser?.let {
            Text(
                text = "Selected: ${it.name} - ${it.accountNumber}",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        SpaceHeight(50.dp)
    }
}


@Composable
fun SelectMultipleFromLazyColumn(){
    var selectedUsers by remember { mutableStateOf(setOf<User>()) }

    Column {
        SpaceHeight(100.dp)
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mockUsers) { user ->
                PersonCard(
                    user = user,
                    isSelected = user in selectedUsers,
                    onClick = {
                        selectedUsers = if (user in selectedUsers) {
                            selectedUsers - user  // Remove if already selected
                        } else {
                            selectedUsers + user  // Add if not selected
                        }
                    }
                )
            }
        }

        // Display selected users' details below the list
        if (selectedUsers.isNotEmpty()) {
            Text(
                text = "Selected Users:",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            selectedUsers.forEach {
                Text(
                    text = "${it.name} - ${it.accountNumber}",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 16.sp
                )
            }
        }
        SpaceHeight(50.dp)
    }
}


@Composable
fun PersonCard(
    modifier: Modifier = Modifier,
    user: User,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color.LightGray else Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(16.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
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