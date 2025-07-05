package dev.rmarcos.performancereviewapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.model.DevelopmentPlan
import dev.rmarcos.performancereviewapp.model.Goal
import dev.rmarcos.performancereviewapp.model.GoalState
import dev.rmarcos.performancereviewapp.model.Permission
import dev.rmarcos.performancereviewapp.model.User
import dev.rmarcos.performancereviewapp.screens.MainScreen
import dev.rmarcos.performancereviewapp.screens.MainScreen.Users
import dev.rmarcos.performancereviewapp.screens.ProfileScreen
import dev.rmarcos.performancereviewapp.screens.UserListScreen
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

@OptIn(ExperimentalMaterial3Api::class)
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
    var title by remember { mutableStateOf("") }
    Scaffold (
        modifier = Modifier,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = title
                    )
                }
            )
        },
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
                title = stringResource(R.string.profile_label)
                ProfileScreen(
                    user = user,
                    navController = navController
                )
            }
            composable (MainScreen.Goals.route) {
                title = stringResource(R.string.goals_label)
                Goals(
                    developmentPlans = mockObject.developmentPlans(),
                    user = user,
                    navController = navController
                )
            }
            composable (MainScreen.Users.route) {
                title = stringResource(R.string.users_label)
                UserListScreen(
                    users = mockObject.users(),
                    navController = navController
                )
            }
            composable (
                MainScreen.Profile.route + "/{username}",
                arguments = listOf(navArgument("username") {
                    type = NavType.StringType
                })
                ) { backtrackEntry ->
                val username = backtrackEntry.arguments!!
                    .getString("username") as String
                title = stringResource(R.string.profile_label) + " — $username"
                ProfileScreen(
                    user = mockObject.getUser(username),
                    navController = navController
                )
            }
        }
    }
}

@Preview(group = "goals", showBackground = true)
@Composable
fun FABPreview() {
    Column {
        ExpandableFloatingActionButton()
        SmallFloatingActionButtonWithLabel(
            icon = painterResource(R.drawable.baseline_add_chart_24),
            label = stringResource(R.string.fab_new_plan),
            onClick = {}
        )
    }
}

@Composable
fun SmallFloatingActionButtonWithLabel(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit = {},
    label: String
) {
    Row (
        modifier = Modifier
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        SmallFloatingActionButton(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            onClick = onClick,

            ) {
            Icon(
                painter = icon,
                contentDescription = null
            )
        }
        Spacer(
            Modifier.padding(4.dp)
        )
        Text(
            text = label
        )
    }
}

@Composable
fun ExpandableFloatingActionButton(
    modifier: Modifier = Modifier
) {
    var isExpended by remember { mutableStateOf(false) }
    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ){
        if (isExpended) {
            SmallFloatingActionButtonWithLabel(
                icon = painterResource(R.drawable.baseline_add_chart_24),
                label = stringResource(R.string.fab_new_plan),
                onClick = {}
            )
            SmallFloatingActionButtonWithLabel(
                icon = painterResource(R.drawable.baseline_playlist_add_24),
                label = stringResource(R.string.fab_new_goal),
                onClick = {}
            )
        }
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            onClick = {
                isExpended = !isExpended
            }
        ) {
            Icon(
                painter = painterResource(
                    if (isExpended)
                        R.drawable.baseline_close_24
                    else
                        R.drawable.baseline_add_24
                ),
                contentDescription = null
            )
        }
    }
}

@Composable
fun GoalListItem(
    modifier: Modifier = Modifier,
    goal: Goal
){
    var isDone by remember { mutableStateOf(goal.state == GoalState.DONE) }
    Column (
        modifier = modifier
            .padding(top = 4.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                modifier = Modifier.weight(3f),
                text = goal.title,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.weight(2f),
                text = "${goal.points}"
            )
            Checkbox(
                modifier = Modifier.weight(1f),
                checked = isDone,
                onCheckedChange = {
                    isDone = !isDone
                    // save modifications
                }
            )
        }
        Text(
            modifier = Modifier.padding(6.dp),
            text = goal.description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(group = "goals", showBackground = true)
@Composable
fun GoalsPreview(){
    Goals(
        developmentPlans = mockObject.developmentPlans(),
        user = mockObject.getUser("lmandume"),
        navController = rememberNavController()
    )
}

@Composable
fun DevelopmentPlanItem(
    modifier: Modifier = Modifier,
    developmentPlan: DevelopmentPlan,
    onClick: () -> Unit = {}
){
    var isExpanded by remember { mutableStateOf(false) }
    Card (
    modifier = modifier
        .padding(top = 16.dp)
        //.fillMaxWidth()
        //.background(MaterialTheme.colorScheme.primaryContainer)
        .clickable(
            onClick = { isExpanded = !isExpanded }
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
){
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.development_plan)
                    + developmentPlan.department.name,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelLarge
        )
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = stringResource(R.string.start_date)
                        + developmentPlan.endDate.toString(),
                fontWeight = FontWeight.Thin
            )
            Text(
                text = stringResource(R.string.end_date)
                        + developmentPlan.endDate.toString(),
                fontWeight = FontWeight.Thin
            )
        }
    }
    if (isExpanded){
        Text(
            modifier = Modifier.padding(8.dp),
            text = stringResource(R.string.goals_title),
            style = MaterialTheme.typography.labelMedium
        )
        mockObject.goals().filter {
            it.developmentPlan == developmentPlan
        }.forEach{ goal ->
            GoalListItem(
                modifier = Modifier.padding(8.dp),
                goal = goal)
        }
        Button(
            modifier = Modifier.padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                //TODO generate chart
            }
        ) {
            Text(
                text = stringResource(R.string.show_progress)
            )
        }
        Spacer(Modifier.padding(bottom = 24.dp))
    }
}
}

@Composable
fun Goals(
    modifier: Modifier = Modifier,
    developmentPlans: List<DevelopmentPlan>,
    user: User,
    navController: NavController
){
    Box (
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {
        LazyColumn (
            modifier = Modifier.wrapContentSize()
        ){
            items(developmentPlans) { developmentPlan ->
                if (user.permission == Permission.ADMIN
                    || (user.permission == Permission.MANAGER
                            && user.profile.department == developmentPlan.department))
                    DevelopmentPlanItem(
                        developmentPlan = developmentPlan
                    )
            }
        }
        ExpandableFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
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