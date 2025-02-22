import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddNickNameDialog(
    onDismissRequest: () -> Unit,
    onLocationAdded: (String) -> Unit
) {
    var nickname by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Important: Make dialog full screen
        )
    ) {
        // Use a Box to fill the screen
        Box(Modifier.fillMaxSize()) {  // Fill the entire screen
            Card(
                modifier = Modifier
                    .fillMaxSize() // Card also fills the screen
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TopAppBar (or similar) for title and actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onDismissRequest) {
                            Icon(androidx.compose.material.icons.Icons.Filled.Close, contentDescription = "Close")
                        }
                        Text(
                            text = "Location Details",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(onClick = { onLocationAdded(nickname) }) {
                            Text("Save")
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Latitude: ",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Longitude: ",
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedTextField(
                            value = nickname,
                            onValueChange = { nickname = it },
                            label = { Text("Location Name") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier.fillMaxWidth().padding(0.dp, 16.dp, 0.dp, 8.dp)
                        )

                        Column {
                            Text(
                                modifier = Modifier.padding(0.dp, 32.dp,8.dp, 8.dp),

                                text = "Save As",
                                style = MaterialTheme.typography.titleMedium

                            )
                            Row {
                                AssistChip(
                                    modifier = Modifier.padding(0.dp, 8.dp, 8.dp, 8.dp),
                                    onClick = { Log.d("Assist chip", "hello world") },
                                    label = { Text("Home") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Home,
                                            contentDescription = "Localized description",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    }
                                )
                                AssistChip(
                                    modifier = Modifier.padding(0.dp, 8.dp, 8.dp, 8.dp),

                                    onClick = { Log.d("Assist chip", "hello world") },
                                    label = { Text("Work") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.AccountBox,
                                            contentDescription = "Localized description",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    }
                                )
                                AssistChip(
                                    modifier = Modifier.padding(0.dp, 8.dp, 8.dp, 8.dp),

                                    onClick = { Log.d("Assist chip", "hello world") },
                                    label = { Text("Other") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = "Localized description",
                                            Modifier.size(AssistChipDefaults.IconSize)
                                        )
                                    }
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}