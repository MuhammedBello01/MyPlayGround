import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SnackbarDemoScreen() {
    var currentSnackbar by remember { mutableStateOf<SnackbarData?>(null) }

    Scaffold { paddingValues ->
        // TopSnackbar should be at the top level of your Scaffold content
        // This is where the snackbar will appear at the top
        TopSnackbar(
            snackbarData = currentSnackbar,
            onDismiss = { currentSnackbar = null }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Snackbar Demo",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    currentSnackbar = SnackbarData(
                        message = "Success! Operation completed",
                        type = SnackbarType.SUCCESS
                    )
                }
            ) {
                Text("Show Success Snackbar")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    currentSnackbar = SnackbarData(
                        message = "Error occurred. Please try again",
                        type = SnackbarType.ERROR
                    )
                }
            ) {
                Text("Show Error Snackbar")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    currentSnackbar = SnackbarData(
                        message = "This is an info message with action",
                        type = SnackbarType.INFO,
                        actionLabel = "Undo",
                        onActionClick = { /* Handle undo action */ }
                    )
                }
            ) {
                Text("Show Info Snackbar With Action")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = {
                    currentSnackbar = SnackbarData(
                        message = "This snackbar stays longer",
                        type = SnackbarType.SUCCESS,
                        duration = 5000L
                    )
                }
            ) {
                Text("Show Long Duration Snackbar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SnackbarDemoPreview() {
    MaterialTheme {
        Surface {
            SnackbarDemoScreen()
        }
    }
}
