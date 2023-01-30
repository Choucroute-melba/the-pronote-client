package moi.choucroutemelba.thepronoteclient.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import moi.choucroutemelba.thepronoteclient.ThePronoteApplication
import moi.choucroutemelba.thepronoteclient.ui.theme.ThePronoteClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (application as ThePronoteApplication).container
        setContent {
            ThePronoteApp(appContainer)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ThePronoteClientTheme {
        Greeting("Android")
    }
}