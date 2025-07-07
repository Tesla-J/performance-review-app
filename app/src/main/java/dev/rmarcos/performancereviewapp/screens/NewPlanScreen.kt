package dev.rmarcos.performancereviewapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
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
import dev.rmarcos.performancereviewapp.model.Department
import dev.rmarcos.performancereviewapp.model.DevelopmentPlan
import dev.rmarcos.performancereviewapp.model.Goal
import dev.rmarcos.performancereviewapp.model.GoalState
import dev.rmarcos.performancereviewapp.model.Permission
import dev.rmarcos.performancereviewapp.model.User
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(state.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel)
                )
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlanScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    user: User
){
    var id: Long = (1..Long.MAX_VALUE).random()
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var endDate by remember { mutableStateOf(LocalDate.now()) }
    var allowedDepartments = mockObject.departments().filter{
        user.profile.department == it ||
                user.permission == Permission.ADMIN
    }
    var department by remember { mutableStateOf(allowedDepartments.get(0)) }
    var isExpanded by remember { mutableStateOf(false) }
    var isStartDateVisible by remember { mutableStateOf(false) }
    var isEndDateVisible by remember { mutableStateOf(false) }
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
                value = department.name,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(R.string.choose_department)
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
                allowedDepartments.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name
                            )
                        },
                        onClick = {
                            department = it
                            isExpanded = !isExpanded
                        }
                    )
                }
            }
        }
        if(isStartDateVisible)
            DatePickerModal(
                onDateSelected = {
                    it.let{
                        startDate = Instant.ofEpochMilli(it!!)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                },
                onDismiss = {
                    isStartDateVisible = !isStartDateVisible
                }
            )
        Button(
            modifier = Modifier
                .width(360.dp),
            onClick = {
                isStartDateVisible = !isStartDateVisible
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ){
            Text(
                text = startDate.toString()
            )
        }
        if (isEndDateVisible)
            DatePickerModal(
                onDateSelected = {
                    it.let{
                        endDate = Instant.ofEpochMilli(it!!)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                },
                onDismiss = {
                    isEndDateVisible = !isEndDateVisible
                }
            )
        Button(
            modifier = Modifier
                .width(360.dp),
            onClick = {
                isEndDateVisible = !isEndDateVisible
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ){
            Text(
                text = endDate.toString()
            )
        }
        Button(
            modifier = Modifier
                .width(360.dp)
                .padding(top = 120.dp),
            enabled = startDate.isBefore(endDate),
            onClick = {
                mockObject.newDevelopmentPlan(DevelopmentPlan(
                    id = id,
                    startDate = startDate,
                    endDate = endDate,
                    department = department
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
fun NewPlanScreenPreview()
{
    NewGoalScreen(
        user = mockObject.users().get(0),
        navController = rememberNavController()
    )
}