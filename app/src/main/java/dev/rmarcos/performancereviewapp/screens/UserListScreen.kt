package dev.rmarcos.performancereviewapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.model.User

@Preview(group = "user-list", showBackground = true)
@Composable
fun UsersPreview() {
    UserListScreen(
        users = mockObject.users(),
        navController = rememberNavController()
    )
}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    navController: NavController
){
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    MainScreen.Profile.route + "/${user.username}"
                )
            }
    ){
        Text(
            modifier = Modifier.padding(top = 8.dp)
                .padding(4.dp),
            text = user.profile.name,
            fontWeight = FontWeight.Bold
        )
        Text (
            modifier = Modifier.padding(4.dp),
            text = user.profile.role,
            fontWeight = FontWeight.Thin
        )
    }
}

@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    users: List<User>,
    navController: NavController
){
    /*Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Users"
        )
    }*/
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        items(users) { user ->
            UserItem(
                user = user,
                navController = navController
            )
        }
    }
}