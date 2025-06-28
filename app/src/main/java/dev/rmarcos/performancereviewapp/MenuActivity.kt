package dev.rmarcos.performancereviewapp

import android.app.Activity
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.rmarcos.performancereviewapp.mock.mockObject
import dev.rmarcos.performancereviewapp.ui.theme.PerformanceReviewAppTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PerformanceReviewAppTheme {
                Surface (modifier = Modifier.fillMaxSize()) {
                    Menu()
                }
            }
        }
    }
}

@Composable
fun Menu(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val intent = when (context) {
        is Activity -> context.intent
        //is ContextWrapper -> context.baseContext
        else -> null
    }
    val user = mockObject.users().filter({
        it.username == intent!!.getStringExtra("USER")
    }).get(0)

    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = user.username
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuPreview() {
    PerformanceReviewAppTheme {
        Menu()
    }
}