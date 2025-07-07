package dev.rmarcos.performancereviewapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import dev.rmarcos.performancereviewapp.R
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.model.DevelopmentPlan
import dev.rmarcos.performancereviewapp.model.Goal
import dev.rmarcos.performancereviewapp.model.GoalState
import dev.rmarcos.performancereviewapp.model.Permission
import dev.rmarcos.performancereviewapp.model.User

@Preview(group = "goals", showBackground = true)
@Composable
fun FABPreview() {
    Column {
        ExpandableFloatingActionButton(
            user = mockObject.users().get(0),
            navController = rememberNavController()
        )
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
        modifier = modifier
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
    modifier: Modifier = Modifier,
    navController: NavController,
    user: User
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
                onClick = {
                    navController.navigate(
                        MainScreen.NewPlan.route + "/${user.username}"
                    )
                }
            )
            SmallFloatingActionButtonWithLabel(
                icon = painterResource(R.drawable.baseline_playlist_add_24),
                label = stringResource(R.string.fab_new_goal),
                onClick = {
                    navController.navigate(
                        MainScreen.NewGoal.route + "/${user.username}"
                    )
                }
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
                    mockObject.goals().forEach {
                        if (it.title == goal.title)
                            it.state = if (isDone) GoalState.DONE else GoalState.FAILED
                    }
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
    GoalsList(
        developmentPlans = mockObject.developmentPlans(),
        user = mockObject.getUser("lmandume"),
        navController = rememberNavController()
    )
}

fun donutChartData(
    developmentPlan: DevelopmentPlan,
    doneLabel: String,
    undoneLabel: String
): PieChartData{
    val departmentGoals = mockObject.goals().filter { it.developmentPlan == developmentPlan }
    val doneDepartmentGoals = departmentGoals.filter { it.state == GoalState.DONE }
    var totalPoints = departmentGoals.sumOf { it.weight * it.points }
    if (totalPoints == 0)
        ++totalPoints
    val totalPointsDone = doneDepartmentGoals.sumOf { it.weight * it.points }
    val colorDone = // TODO Why the hell this works?
    return PieChartData(
        slices = listOf(
            PieChartData.Slice(
                doneLabel,
                (totalPointsDone / totalPoints) + 0f,// * 100f,
                Color(0xff00d3f2)
            ),
            PieChartData.Slice(
                undoneLabel,
                ((totalPoints - totalPointsDone) / totalPoints) + 0f,// * 100f,
                Color(0xffff6467) // second option #fb2c36
            )
        ),
        plotType = PlotType.Donut
    )
}

fun donutChartConfig(
    bgColor: Color
): PieChartConfig{
    return PieChartConfig(
        //percentVisible = true,
        //percentageFontSize = 42.sp,
        strokeWidth = 120f,
        //percentColor = MaterialTheme.colorScheme.primary,
        activeSliceAlpha = .9f,
        isAnimationEnable = true,
        labelVisible = true,
        backgroundColor = bgColor
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
            /*Button(
                modifier = Modifier.padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    //TODO generate chart
                }
            ) {
                Text(
                    text = stringResource(R.string.show_progress)
                )
            }*/
            DonutPieChart(
                modifier = Modifier.wrapContentSize(),
                pieChartData = donutChartData(
                    developmentPlan = developmentPlan,
                    doneLabel = stringResource(R.string.goals_done),
                    undoneLabel = stringResource(R.string.goals_unfinished)
                ),
                pieChartConfig = donutChartConfig(
                    //developmentPlan = developmentPlan,
                    bgColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            Spacer(Modifier.padding(bottom = 24.dp))
        }
    }
}

@Composable
fun GoalsList(
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
                .padding(16.dp),
            user = user,
            navController = navController
        )
    }
}