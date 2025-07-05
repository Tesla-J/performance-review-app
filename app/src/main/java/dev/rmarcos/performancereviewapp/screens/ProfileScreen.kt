package dev.rmarcos.performancereviewapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.rmarcos.performancereviewapp.R
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.model.User

@Composable
fun ProfileField(key: String, value: String){
    Row (
        modifier = Modifier
            .padding(4.dp)
    ){
        Text(
            modifier = Modifier.weight(1f),
            text = key,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.weight(1f),
            text = value
        )
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user:User,
    navController: NavController
){
    val profile = user.profile
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        ProfileField(stringResource(R.string.profile_name), profile.name)
        ProfileField(stringResource(R.string.profile_email), user.email)
        ProfileField(stringResource(R.string.profile_role), profile.role)
        ProfileField(stringResource(R.string.profile_phone), profile.phone)
        ProfileField(stringResource(R.string.profile_alt_phone), profile.altPhone)
        ProfileField(stringResource(R.string.profile_department_name), profile.department.name)
    }
}

@Preview(group = "profile_preview", showSystemUi = true)
@Composable
fun ProfilePreview()
{
    ProfileScreen(
        user = mockObject.users().get(0),
        navController = rememberNavController()
    )
}