package ru.zveron

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.zveron.ui.theme.ZveronTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZveronTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android", viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, viewModel: MainViewModel) {
    val responseState by viewModel.messageResult.collectAsState()

    Column {
        Text(text = "Hello $name!")

        Spacer(modifier = Modifier.size(16.dp))

        Text(text = "Response: $responseState")

        Spacer(modifier = Modifier.size(40.dp))

        Button(onClick = { viewModel.onSendClick() }, modifier = Modifier.fillMaxWidth()) {
            Text("Send request")
        }
    }
}
