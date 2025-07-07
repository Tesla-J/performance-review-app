package dev.rmarcos.performancereviewapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.rmarcos.performancereviewapp.R
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.model.DevelopmentPlan
import dev.rmarcos.performancereviewapp.model.Goal
import dev.rmarcos.performancereviewapp.model.GoalState
import dev.rmarcos.performancereviewapp.model.Permission
import dev.rmarcos.performancereviewapp.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGoalScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    user: User
){
    var id: Long = (1..Long.MAX_VALUE).random()
    var title by remember { mutableStateOf("") }
    var description by remember {mutableStateOf("")}
    var weight by remember { mutableStateOf("1") }
    var points by remember { mutableStateOf("10") }
    val state = GoalState.FAILED
    val allowedDevelopmentPlan = mockObject.developmentPlans().filter {
        it.department == user.profile.department ||
                user.permission == Permission.ADMIN
    }
    var developmentPlan by remember { mutableStateOf(allowedDevelopmentPlan.get(0)) }
    var isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded},
        ) {
            TextField(
                modifier = Modifier.padding(16.dp).width(360.dp)
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                value = developmentPlan.department.name,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(R.string.choose_development_plan)
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(isExpanded)
                }
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false}
            ) {
                allowedDevelopmentPlan.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(R.string.development_plan) + it.department.name
                            )
                        },
                        onClick = {
                            developmentPlan = it
                            isExpanded = !isExpanded // TODO
                        }
                    )
                }
            }
        }
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .width(360.dp),
            value = title,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.title)) },
            onValueChange = { newText ->
                title = newText
            }
        )
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .width(360.dp),
            value = description,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.description)) },
            onValueChange = { newText ->
                description = newText
            }
        )
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .width(360.dp),
            value = points,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            label = { Text(stringResource(R.string.points)) },
            onValueChange = { points = it }
        )
        TextField(
            modifier = Modifier
                .padding(8.dp)
                .width(360.dp),
            value = weight,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            label = { Text(stringResource(R.string.weight)) },
            placeholder = { Text("password") },
            onValueChange = { newText ->
                weight = newText
            }
        )
        Button(
            modifier = Modifier
                .width(360.dp),
            enabled = title.isNotBlank() && description.isNotBlank() &&
                    weight.isNotBlank() && points.isNotBlank(),
            onClick = {
                mockObject.newGoal(Goal(
                    id = id,
                    title = title,
                    description = description,
                    weight = weight.toInt(),
                    points = points.toInt(),
                    developmentPlan = developmentPlan,
                    state = state
                ))
                navController.popBackStack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ){
            Text(
                text = stringResource(R.string.save)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun NewGoalScreenPreview()
{
    NewGoalScreen(
        user = mockObject.users().get(0),
        navController = rememberNavController()
    )
}