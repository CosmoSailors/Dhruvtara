import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.grasstools.tangential.domain.model.GeofenceType
import com.grasstools.tangential.presentation.ui.mapscreen.MapsViewModel

@Composable
fun AddNickNameDialog(
    onDismissRequest: () -> Unit,
    onLocationAdded: (String) -> Unit,
    latitude: Double,
    longitude: Double,
    radius: Float,
    vm: MapsViewModel
) {
    var nickname by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                DialogHeader(onDismissRequest, onLocationAdded, nickname)
                DialogContent(
                    nickname = nickname,
                    latitude = latitude,
                    longitude = longitude,
                    radius = radius,
                    onNicknameChange = { nickname = it },
                    vm = vm
                )
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
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDismissRequest) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
        Text(
            text = "Trigger Details",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = { onLocationAdded(nickname) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Save", color = MaterialTheme.colorScheme.onPrimary)
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
    radius: Float,
    vm: MapsViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val locationTypes = listOf("DND", "Alarm")

    val type by vm.type.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 🔹 Location Info
        Text(
            text = "Location Info",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Latitude: $latitude", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Longitude: $longitude", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Radius: ${radius}m", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Nickname Field
        OutlinedTextField(
            value = nickname,
            onValueChange = onNicknameChange,
            label = { Text("Location Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Dropdown Menu for Selecting Location Type
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = type.toString(),
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
                locationTypes.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            vm.updateType(GeofenceType.valueOf(item))
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Save As Section with Assist Chips
        Text(
            text = "Save As",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SaveAsChips()
    }
}

@Composable
private fun SaveAsChips() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
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
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
