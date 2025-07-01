package dev.rmarcos.performancereviewapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.model.Goal
import dev.rmarcos.performancereviewapp.model.Permission
import dev.rmarcos.performancereviewapp.model.User
import dev.rmarcos.performancereviewapp.screens.MainScreen
import dev.rmarcos.performancereviewapp.ui.theme.PerformanceReviewAppTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerformanceReviewAppTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    val user = mockObject.getUser(
                        intent.getStringExtra(
                            MainActivity.USER_EXTRA_KEY
                        ) as String
                    )
                    Menu(user = user)
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    icon: Painter, label: String,
    contentDescritpion: String,
    selected: Boolean = false
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .background(if (selected) Color(0xff0C7489) else MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier,
            painter = icon,
            contentDescription = contentDescritpion
        )
        Spacer(
            modifier = Modifier.padding(2.dp),
        )
        Text(
            text = label
        )
    }
}

@Composable
fun Menu(modifier: Modifier = Modifier, user: User) {
    /*val context = LocalContext.current
    val intent = when (context) {
        is Activity -> context.intent
        //is ContextWrapper -> context.baseContext
        else -> null
    }*/
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        BottomNavigationBar(user = user)
    }
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, user: User) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val onClick: (Int, MainScreen) -> Unit = { index,screen ->
        selectedItem = index
        navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    Scaffold (
        modifier = Modifier,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    label = {
                        Text(
                            text = stringResource(R.string.profile_label)
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_person_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onClick(0, MainScreen.Profile)
                    }
                )
                NavigationBarItem(
                    enabled = user.permission == Permission.MANAGER
                            || user.permission == Permission.ADMIN,
                    selected = selectedItem == 1,
                    label = {
                        Text(
                            text = stringResource(R.string.goals_label)
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_checklist_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onClick(1, MainScreen.Goals)
                    }
                )
                NavigationBarItem(
                    enabled = user.permission == Permission.ADMIN,
                    selected = selectedItem == 2,
                    label = {
                        Text(
                            text = stringResource(R.string.users_label)
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_groups_24),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onClick(2, MainScreen.Users)
                    }
                )

            }
        }
    ){ paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Profile.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable (MainScreen.Profile.route) {
                Profile(
                    user = user,
                    navController = navController
                )
            }
            composable (MainScreen.Goals.route) {
                Goals(
                    goals = mockObject.goals(),
                    navController = navController
                )
            }
            composable (MainScreen.Users.route) {
                Users(
                    users = mockObject.users(),
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun Profile(
    modifier: Modifier = Modifier,
    user:User,
    navController: NavController
){
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile"
        )
    }
}

@Composable
fun Goals(
    modifier: Modifier = Modifier,
    goals: List<Goal>,
    navController: NavController
){
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Goals"
        )
    }
}

@Composable
fun Users(
    modifier: Modifier = Modifier,
    users: List<User>,
    navController: NavController
){
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Users"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuPreview() {
    PerformanceReviewAppTheme {
        Menu(user = mockObject.users().get(0))
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemPreview() {
    MenuItem(
        icon = painterResource(R.drawable.baseline_groups_24),
        label = "Profiles",
        contentDescritpion = "Users",
        selected = true
    )
}