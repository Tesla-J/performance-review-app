package dev.rmarcos.performancereviewapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.rmarcos.performancereviewapp.ui.theme.PerformanceReviewAppTheme
import dev.rmarcos.performancereviewapp.mock.mockObject

class MainActivity : ComponentActivity() {
    companion object Factory {
        val USER_EXTRA_KEY = "USERNAME_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerformanceReviewAppTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    Login()
                }
            }
        }
    }
}

@Composable
fun Login(modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isWrongLogin by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xff0C7489)),
        contentAlignment = Alignment.Center
    ) {
        if (isWrongLogin){
            WrongLoginDialog {
                isWrongLogin = false
            }
        }
        Column (
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                value = username,
                label = { Text("Username") },
                placeholder = { Text("username") },
                onValueChange = { newText ->
                    username = newText
                }
            )
            TextField(
                modifier = Modifier.padding(8.dp),
                value = password,
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                label = { Text("Password") },
                placeholder = { Text("password") },
                onValueChange = { newText ->
                    password = newText
                }
            )
            Button(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(Color(0xffdbeafe)),
                onClick = { // check login details
                    mockObject.users().forEach { user ->
                        if (user.username == username &&
                            user.password == password) {
                            // call another activity
                            val intent = Intent(context, MenuActivity::class.java)
                            intent.putExtra(MainActivity.USER_EXTRA_KEY, user.username)
                            context.startActivity(intent)
                            return@Button
                        }
                        isWrongLogin = true;
                    }
                }
            ){
                Text(
                    "Login",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Contact your company's admin in case you have problems accessing your account",
                textAlign = TextAlign.Center,
                color = Color.White
            )

        }
    }
}

@Composable
fun WrongLoginDialog(
    text:String = "Wrong login details!",
    onDismissRequest: () -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .background(Color(0xffdbeafe)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun LoginPreview() {
    Login()
}