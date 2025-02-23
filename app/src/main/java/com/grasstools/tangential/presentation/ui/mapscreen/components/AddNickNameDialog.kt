import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.grasstools.tangential.presentation.ui.mainscreen.viewmodel

@Composable
fun AddNickNameDialog(
    onDismissRequest: () -> Unit,
    onLocationAdded: (String) -> Unit,
    latitude: Double,
    longitude: Double,
    radius: Float
) {
    var nickname by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                DialogHeader(onDismissRequest, onLocationAdded, nickname)
                DialogContent(
                    nickname,
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius,
                    onNicknameChange = { nickname = it })
            }
        }
    }
}

@Composable
private fun DialogHeader(
    onDismissRequest: () -> Unit,
    onLocationAdded: (String) -> Unit,
    nickname: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDismissRequest) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
        Text(text = "Trigger Details", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = { onLocationAdded(nickname) }) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogContent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    latitude: Double,
    longitude: Double,
    radius: Float
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Select Type") }
    val locationTypes = listOf("DND", "Alarm")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Column(Modifier.weight(1f)) {
                Text(text = "Latitude: $latitude", style = MaterialTheme.typography.titleMedium)
                Text(text = "Longitude: $longitude", style = MaterialTheme.typography.titleMedium)
            }
        }
        Text(text = "Radius: ${radius}m", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = { Text("Location Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // 🔹 Dropdown menu for selecting location type
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Type") },
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                locationTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(
            text = "Save As",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 32.dp)
        )
        SaveAsChips()
    }
}


@Composable
private fun SaveAsChips() {
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf(
            "Home" to Icons.Filled.Home,
            "Work" to Icons.Filled.AccountBox,
            "Other" to Icons.Filled.Favorite
        ).forEach { (label, icon) ->
            AssistChip(
                modifier = Modifier.padding(end = 8.dp),
                onClick = { Log.d("AssistChip", "$label clicked") },
                label = { Text(label) },
                leadingIcon = {
                    Icon(
                        icon,
                        contentDescription = "$label Icon",
                        Modifier.size(AssistChipDefaults.IconSize)
                    )
                }
            )
        }
    }
}
